package com.amiculous.nasaview.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.api.ApodApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Database(entities = {ApodEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "nasa_media_db";
    private static final String API_KEY = BuildConfig.API_KEY;

    private static AppDatabase INSTANCE;

    //use singleton to prevent having multiple instances of the database opened at same tiome
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(appDatabaseCallback)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }

    //abstract getter methods for each @Dao
    public abstract ApodFavoritesDao apodFavoritesDao();

    private static AppDatabase.Callback appDatabaseCallback = new AppDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final ApodFavoritesDao apodFavoritesDao;

        PopulateDbAsync(AppDatabase db) {apodFavoritesDao = db.apodFavoritesDao();}
        String explanation = "Magnificent spiral galaxy NGC 4565 is viewed edge-on from planet Earth. Also known as the Needle Galaxy for its narrow profile, bright NGC 4565 is a stop on many telescopic tours of the northern sky, in the faint but well-groomed constellation Coma Berenices. This sharp, colorful image reveals the galaxy's bulging central core cut by obscuring dust lanes that lace NGC 4565's thin galactic plane. An assortment of other background galaxies is included in the pretty field of view, with neighboring galaxy NGC 4562 at the upper right. NGC 4565 itself lies about 40 million light-years distant and spans some 100,000 light-years.  Easily spotted with small telescopes, sky enthusiasts consider NGC 4565 to be a prominent celestial masterpiece Messier missed.";
        ApodEntity apod = new ApodEntity("Christoph Kaltseis","2019-02-22",explanation,"image","NGC 4565: Galaxy on Edge","https://apod.nasa.gov/apod/image/1902/N4565ps06d_35tp_Kaltseis2019_1024.jpg");

        @Override
        protected Void doInBackground(final Void... params) {
            apodFavoritesDao.deleteAll();

            apodFavoritesDao.insertApod(apod);
            return null;
        }


    }

}