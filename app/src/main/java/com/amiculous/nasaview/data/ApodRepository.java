package com.amiculous.nasaview.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ApodRepository {

    private ApodFavoritesDao apodFavoritesDao;
    private LiveData<List<ApodEntity>> allFavoriteApods;

    ApodRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        apodFavoritesDao = db.apodFavoritesDao();
        allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
    }

    LiveData<List<ApodEntity>> getAllFavoriteApods() {
        return allFavoriteApods;
    }

    public void insertApod(ApodEntity apodEntity) {
        new insertApodAsyncTask(apodFavoritesDao).execute(apodEntity);
    }

    private static class insertApodAsyncTask extends AsyncTask<ApodEntity, Void, Void> {

        private ApodFavoritesDao apodFavoritesAsyncDao;

        insertApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final ApodEntity... params) {
            apodFavoritesAsyncDao.insertApod(params[0]);
            return null;
        }
    }

}
