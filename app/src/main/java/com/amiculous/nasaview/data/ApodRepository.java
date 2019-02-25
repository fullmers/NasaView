package com.amiculous.nasaview.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import timber.log.Timber;

public class ApodRepository {

    private ApodFavoritesDao apodFavoritesDao;
    private LiveData<List<ApodEntity>> allFavoriteApods;

    public ApodRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        apodFavoritesDao = db.apodFavoritesDao();
        allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
    }

    public LiveData<ApodEntity> getApod(String date) {
        Timber.i("getting apod from " + date);
        return apodFavoritesDao.loadApod(date);
    }

    public LiveData<List<ApodEntity>> getAllFavoriteApods() {
        Timber.i("getting all favorites from database");
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
        Timber.i("inserting " + params[0].getTitle() + "into database");
            apodFavoritesAsyncDao.insertApod(params[0]);
            return null;
        }
    }

    public void deleteApod(String date) {
        new deleteApodAsyncTask(apodFavoritesDao).execute(date);
    }
    private static class deleteApodAsyncTask extends AsyncTask<String, Void, Void> {
        private ApodFavoritesDao apodFavoritesAsyncDao;

        deleteApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            Timber.i("deleting apod from " + params[0] + "from database");
            apodFavoritesAsyncDao.deleteApod(params[0]);
            return null;
        }
    }

}
