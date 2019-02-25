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

    public LiveData<List<ApodEntity>> getAllFavoriteApods() {
        Timber.i("getting all favorites from database");
        List<ApodEntity> apodList = allFavoriteApods.getValue();
        if (apodList != null) {
       for(ApodEntity apod: apodList) {
            Timber.i(apod.getTitle());
        }}

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
            LiveData<List<ApodEntity>> apods = apodFavoritesAsyncDao.loadAllFavoriteApods();
            List<ApodEntity> apodList = apods.getValue();
            if(apodList!=null) {
                Timber.i("apodList is not null");
            for(ApodEntity apod: apodList) {
                Timber.i(apod.getTitle());
            }}
            else {Timber.i("apodList IS null");}

            return null;
        }
    }

    public void deleteApod(ApodEntity apodEntity) {
        new deleteApodAsyncTask(apodFavoritesDao).execute(apodEntity);
    }

    private static class deleteApodAsyncTask extends AsyncTask<ApodEntity, Void, Void> {

        private ApodFavoritesDao apodFavoritesAsyncDao;

        deleteApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final ApodEntity... params) {
            Timber.i("deleting " + params[0].getTitle() + "from database");
            apodFavoritesAsyncDao.deleteApod(params[0]);
            return null;
        }
    }

}
