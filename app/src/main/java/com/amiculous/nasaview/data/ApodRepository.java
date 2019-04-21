package com.amiculous.nasaview.data;

import android.app.Application;
import android.os.AsyncTask;

import com.amiculous.nasaview.api.NetworkUtils;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;
import timber.log.Timber;

public class ApodRepository {

    private static ApodFavoritesDao apodFavoritesDao;
    private final LiveData<List<ApodEntity>> allFavoriteApods;
    private final LiveData<ApodEntity> apod;
    private final ApodCallback callback;

    public ApodRepository(Application application, String date, ApodCallback callback) {
        Timber.i("constructing repository");
        AppDatabase db = AppDatabase.getInstance(application);
        apodFavoritesDao = db.apodFavoritesDao();
        allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
        apod = apodFavoritesDao.loadApod(date);
        this.callback = callback;
    }

    public LiveData<ApodEntity> getApod(final String date) {
        Timber.i("calling getApod in repositroy");
        ApodRefreshAsyncInput asyncInput = new ApodRefreshAsyncInput(date, callback,apodFavoritesDao);
        new refreshApodAsyncTask().execute(asyncInput);
        return apod;
    }

    private static class refreshApodAsyncTask extends AsyncTask<ApodRefreshAsyncInput, Void, Void> {

        @Override
        protected Void doInBackground(ApodRefreshAsyncInput... apodRefreshAsyncInput) {
            String date = apodRefreshAsyncInput[0].getDate();
            ApodFavoritesDao apodFavoritesAsyncDao = apodRefreshAsyncInput[0].getApodFavoritesDao();
            ApodCallback callback = apodRefreshAsyncInput[0].getCallback();
            Timber.i("refreshing apod with id = " + date + " from database");
            if (!apodFavoritesAsyncDao.hasApod(date)) {
        //    if (true) {
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
                            callback.wasSuccessful(true);
                        } catch (JsonSyntaxException e) {
                            Crashlytics.logException(e);
                            callback.wasSuccessful(false);
                        }

                        Timber.i(response);
                    } catch (IOException e) {
                        Crashlytics.logException(e);
                        callback.wasSuccessful(false);
                    }
                } else {
                    callback.wasSuccessful(false);
                }


            /*    ApodApi apodApi = ApodApi.retrofit.create(ApodApi.class);
                final Call<ApodEntity> call = apodApi.getApod(API_KEY);
                Timber.i(call.request().url().toString());
                call.enqueue(new Callback<ApodEntity>() {
                    @Override
                    public void onResponse(Call<ApodEntity> call, Response<ApodEntity> response) {
                        ApodEntity todaysApod = response.body();
                        insertApod(todaysApod);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApodEntity> call, @NonNull Throwable t) {}
                });*/
            } else {
                Timber.i("apod WAS in db");
            }
            return null;
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

    public void deleteApod(long id) {
        new deleteApodAsyncTask(apodFavoritesDao).execute(id);
    }

    private static class deleteApodAsyncTask extends AsyncTask<Long, Void, Void> {
        private final ApodFavoritesDao apodFavoritesAsyncDao;

        deleteApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            Timber.i("deleting apod with id = " + params[0] + " from database");
            apodFavoritesAsyncDao.deleteApod(params[0]);
            return null;
        }
    }

    public void markFavorite(ApodEntity apodEntity) {
        Timber.i("calling markFavorite in the ApodRepository. isFavorite = %s", apodEntity.getIsFavorite());
        new markFavoriteAsyncTask(apodFavoritesDao).execute(apodEntity);
    }

    private static class markFavoriteAsyncTask extends AsyncTask<ApodEntity, Void, Void> {
        private final ApodFavoritesDao apodFavoritesAsyncDao;

        markFavoriteAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final ApodEntity... params) {
            Timber.i("marking favorite where id = " + params[0].getId() + "from database");
            ApodEntity apodEntity = params[0];
            if (apodEntity.getIsFavorite()) {
                Timber.i("marking as favorite");
                apodFavoritesAsyncDao.markFavorite(apodEntity.getId());
            } else {
                Timber.i("marking as NOT favorite");
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
