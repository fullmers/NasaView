package com.amiculous.nasaview.data;

import android.app.Application;
import android.os.AsyncTask;

import com.amiculous.nasaview.api.NetworkUtils;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

import timber.log.Timber;

public class ApodRepository {

    public enum ApodCallState {
        HAS_NOT_TRIED, WAITING, SUCCESSFUL, FAILED
    }

    private static ApodFavoritesDao apodFavoritesDao;
    private static LiveData<List<ApodEntity>> allFavoriteApods;
    private final LiveData<ApodEntity> apod;
    private ApodRefreshAsyncInput asyncInput;

    public static void initDao(Application application) {
        if (apodFavoritesDao == null) {
            AppDatabase db = AppDatabase.getInstance(application);
            apodFavoritesDao = db.apodFavoritesDao();
            allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
        }
    }

    public ApodRepository(Application application, String date, ApodCallback callback) {
        Timber.i("constructing repository");
        initDao(application);
        asyncInput = new ApodRefreshAsyncInput(date,callback,apodFavoritesDao);
        apod = apodFavoritesDao.loadApod(date);
    }

    public LiveData<ApodEntity> getApod() {
        try {
            new refreshApodAsyncTask().execute(asyncInput).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return apod;
    }

    private static class refreshApodAsyncTask extends AsyncTask<ApodRefreshAsyncInput, Void, String> {

        @Override
        protected String doInBackground(ApodRefreshAsyncInput... apodRefreshAsyncInput) {
            String date = apodRefreshAsyncInput[0].getDate();
            ApodFavoritesDao apodFavoritesAsyncDao = apodRefreshAsyncInput[0].getApodFavoritesDao();
            ApodCallback callback = apodRefreshAsyncInput[0].getCallback();
            callback.setCallState(ApodCallState.WAITING);
            Timber.i("refreshing apod with id = " + date + " from database");
            if (!apodFavoritesAsyncDao.hasApod(date)) {
                Timber.i("apod was NOT in db");
                URL ApodUrl = NetworkUtils.buildUrl();
                if (ApodUrl != null) {
                    try {
                        Timber.i("trying to fetch with network utils");
                        String response = NetworkUtils.getResponseFromHttpUrl(ApodUrl);
                        try {
                            ApodEntity todaysApod = NetworkUtils.jsonToApod(response);
                            insertApod(todaysApod);
                            Timber.i("inserting response into db");
                            callback.setApod(todaysApod);
                            callback.setCallState(ApodCallState.SUCCESSFUL);
                        } catch (JsonSyntaxException e) {
                            Crashlytics.logException(e);
                            callback.setCallState(ApodCallState.FAILED);
                        }
                    } catch (IOException e) {
                        Crashlytics.logException(e);
                        callback.setCallState(ApodCallState.FAILED);
                    }
                } else {
                    callback.setCallState(ApodCallState.FAILED);
                }
            } else {
                callback.setCallState(ApodCallState.SUCCESSFUL);
                Timber.i("apod WAS in db");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    private static void insertApod(ApodEntity apod) {
        Timber.i("insertApod");
        new insertApodAsyncTask(apodFavoritesDao).execute(apod);
    }

    private static class insertApodAsyncTask extends AsyncTask<ApodEntity, Void, Void> {

        private final ApodFavoritesDao apodFavoritesAsyncDao;
        insertApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }
        @Override
        protected Void doInBackground(final ApodEntity... params) {
            Timber.i("inserting apod with id = " + params[0] + " from database");
            ApodEntity apod = params[0];
            apodFavoritesAsyncDao.insertApod(apod);
            return null;
        }
    }

    public LiveData<List<ApodEntity>> getAllFavoriteApods() {
        Timber.i("getting all favorites from database");
        return allFavoriteApods;
    }

    public static void markFavorite(ApodEntity apodEntity, boolean markAsFavorite) {
        Timber.i("calling markFavorite in the ApodRepository. isFavorite = %s", apodEntity.getIsFavorite());
        new markFavoriteAsyncTask(apodFavoritesDao, markAsFavorite).execute(apodEntity);
    }

    private static class markFavoriteAsyncTask extends AsyncTask<ApodEntity, Void, Void> {
        private final ApodFavoritesDao apodFavoritesAsyncDao;
        private final boolean markAsFavorite;

        markFavoriteAsyncTask(ApodFavoritesDao dao, boolean markAsFavorite) {
            this.apodFavoritesAsyncDao = dao;
            this.markAsFavorite = markAsFavorite;
        }

        @Override
        protected Void doInBackground(final ApodEntity... params) {
            Timber.i("marking favorite where id = " + params[0].getId() + "from database");
            ApodEntity apodEntity = params[0];
            if (markAsFavorite) {
                apodFavoritesAsyncDao.markFavorite(apodEntity.getId());
            } else {
                apodFavoritesAsyncDao.markNotFavorite(apodEntity.getId());
            }
            return null;
        }
    }

    private class ApodRefreshAsyncInput {
        ApodRefreshAsyncInput(String date, ApodCallback callback, ApodFavoritesDao dao) {
            this.date = date;
            this.callback = callback;
            this.dao = dao;
        }

        final ApodFavoritesDao dao;

        ApodFavoritesDao getApodFavoritesDao() {
            return dao;
        }

        final String date;

        String getDate() {
            return date;
        }

        ApodCallback getCallback() {
            return callback;
        }

        final ApodCallback callback;
    }
}
