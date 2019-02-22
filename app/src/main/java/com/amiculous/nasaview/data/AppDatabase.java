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

@Database(entities = {ApodEntity.class}, version = 1, exportSchema = false)
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
        ApodEntity apod;

        @Override
        protected Void doInBackground(final Void... params) {
            ApodApi apodApi= ApodApi.retrofit.create(ApodApi.class);
            final Call<ApodEntity> call = apodApi.getApod(API_KEY);

            call.enqueue(new retrofit2.Callback<ApodEntity>() {
                @Override
                public void onResponse(Call<ApodEntity> call, Response<ApodEntity> response) {
                    apod = response.body();
                    apodFavoritesDao.insertApod(apod);
               }
                @Override
                public void onFailure(@NonNull Call<ApodEntity> call, @NonNull Throwable t) {
                    Log.d("AppDatabase",t.getMessage());
                }
            });

            return null;
        }


    }

}