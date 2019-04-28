package com.amiculous.nasaview.data;

import android.content.Context;
import android.os.AsyncTask;

import com.amiculous.nasaview.BuildConfig;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import timber.log.Timber;

@Database(entities = {ApodEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "nasa_media_db";
    private static final String API_KEY = BuildConfig.API_KEY;

    private static AppDatabase INSTANCE;

    //use singleton to prevent having multiple instances of the database opened at same time
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                Timber.d("Creating new database instance");
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(appDatabaseCallback)
                        .build();
            }
        }
        Timber.d("Getting the database instance");
        return INSTANCE;
    }

    //abstract getter methods for each @Dao
    public abstract ApodFavoritesDao apodFavoritesDao();

    private static final AppDatabase.Callback appDatabaseCallback = new AppDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
         //   new PopulateDbAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final ApodFavoritesDao apodFavoritesDao;

        PopulateDbAsync(AppDatabase db) {apodFavoritesDao = db.apodFavoritesDao();}
        final String explanation1 = "Magnificent spiral galaxy NGC 4565 is viewed edge-on from planet Earth. Also known as the Needle Galaxy for its narrow profile, bright NGC 4565 is a stop on many telescopic tours of the northern sky, in the faint but well-groomed constellation Coma Berenices. This sharp, colorful image reveals the galaxy's bulging central core cut by obscuring dust lanes that lace NGC 4565's thin galactic plane. An assortment of other background galaxies is included in the pretty field of view, with neighboring galaxy NGC 4562 at the upper right. NGC 4565 itself lies about 40 million light-years distant and spans some 100,000 light-years.  Easily spotted with small telescopes, sky enthusiasts consider NGC 4565 to be a prominent celestial masterpiece Messier missed.";
        final ApodEntity apod1 = new ApodEntity("Christoph Kaltseis","2019-02-22",explanation1,"image","NGC 4565: Galaxy on Edge",null,"https://apod.nasa.gov/apod/image/1902/N4565ps06d_35tp_Kaltseis2019_1024.jpg");

        final String explanation2 = "NGC 2359 is a helmet-shaped cosmic cloud with wing-like appendages popularly called Thor's Helmet. Heroically sized even for a Norse god, Thor's Helmet is about 30 light-years across. In fact, the helmet is more like an interstellar bubble, blown as a fast wind from the bright, massive star near the bubble's center inflates a region within the surrounding molecular cloud. Known as a Wolf-Rayet star, the central star is an extremely hot giant thought to be in a brief, pre-supernova stage of evolution. NGC 2359 is located about 15,000 light-years away in the constellation Canis Major. The remarkably detailed image is a mixed cocktail of data from broadband and narrowband filters that captures natural looking stars and the glow of the nebula's filamentary structures. It highlights a blue-green color from strong emission due to oxygen atoms in the glowing gas.";
        final ApodEntity apod2nocopyright = new ApodEntity(null,"2019-02-16",explanation2,"image","NGC 2359: Thor's Helmet",null,"https://apod.nasa.gov/apod/image/1902/thor_LHORHGOBO_final1024.jpg");

        final String explanation3 = "The methane mystery on Mars just got stranger. New results from ESA and Roscosmos' ExoMars Trace Gas Orbiter, has unexpectedly not detected methane in the atmosphere of Mars. This result follows the 2013 detection of methane by NASA's Curiosity rover, a result seemingly confirmed by ESA's orbiting Mars Express the next day. The issue is so interesting because life is a major producer of methane on Earth, leading to intriguing speculation that some sort of life -- possibly microbial life -- is creating methane beneath the surface of Mars. Non-biological sources of methane are also possible. Pictured is a visualization of the first claimed methane plume over Mars as detected from Earth in 2003. The new non-detection of methane by the ExoMars Orbiter could mean that Mars has some unexpected way of destroying methane, or that only some parts of Mars release methane -- and possibly only at certain times. As the mystery has now deepened, humanity's scrutiny of our neighboring planet's atmosphere will deepen as well.";
        final ApodEntity apod3video = new ApodEntity("some copyright", "2019-04-22",explanation3,"video","Mars Methane Mystery Deepens",null,"https://www.youtube.com/embed/rKBoYvVirwA?rel=0");


        @Override
        protected Void doInBackground(final Void... params) {
            apodFavoritesDao.deleteAll();
            apod1.setIsFavorite(true);
            apodFavoritesDao.insertApod(apod1);
            apod2nocopyright.setIsFavorite(true);
            apodFavoritesDao.insertApod(apod2nocopyright);
            apod3video.setIsFavorite(true);
            apodFavoritesDao.insertApod(apod3video);
            return null;
        }
    }


}