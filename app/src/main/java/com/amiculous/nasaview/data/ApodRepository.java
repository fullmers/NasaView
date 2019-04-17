package com.amiculous.nasaview.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.api.NetworkUtils;
import com.amiculous.nasaview.ui.apod.MyCallback;
import com.amiculous.nasaview.ui.apod.SingleApodViewModel;
import com.amiculous.nasaview.ui.apod.SingleApodViewModelFactory;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;
import timber.log.Timber;

import static com.amiculous.nasaview.BuildConfig.API_KEY;

public class ApodRepository {

    private static ApodFavoritesDao apodFavoritesDao;
    private LiveData<List<ApodEntity>> allFavoriteApods;
    private LiveData<ApodEntity> apod;
    private static Context context;

    public ApodRepository(Application application, String date) {
        Timber.i("constructing repository");
        AppDatabase db = AppDatabase.getInstance(application);
        apodFavoritesDao = db.apodFavoritesDao();
        allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
        apod = apodFavoritesDao.loadApod(date);
        context = application.getApplicationContext();
    }

    public LiveData<ApodEntity> getApod(final String date) {
        Timber.i("calling getApod in repositroy");
        new refreshApodAsyncTask(apodFavoritesDao).execute(date);
        return apod;
    }

    private static class refreshApodAsyncTask extends AsyncTask<String, Void, Void> {
        private ApodFavoritesDao apodFavoritesAsyncDao;

        refreshApodAsyncTask(ApodFavoritesDao dao) {
            apodFavoritesAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            Timber.i("refreshing apod with id = " + params[0] + " from database");
            String date = params[0];
            if (!apodFavoritesAsyncDao.hasApod(date)) {
                Timber.i("apod was NOT in db");

                URL ApodUrl = NetworkUtils.buildUrl(context);

                try {
                    Timber.i("trying to fetch with network utils");
                    String response = NetworkUtils.getResponseFromHttpUrl(ApodUrl);
                    try {
                        ApodEntity todaysApod = NetworkUtils.jsonToApod(response);
                        insertApod(todaysApod);
                        Timber.i("inserting response into db");
                    } catch (JsonSyntaxException e) {
                        Timber.i(" could not parse response");
                        //todo do something here
                    }

                    Timber.i(response);
                } catch (IOException e) {
                    e.printStackTrace();
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

        private ApodFavoritesDao apodFavoritesAsyncDao;
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
        private ApodFavoritesDao apodFavoritesAsyncDao;

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
        Timber.i("calling markFavorite in the ApodRepository. isFavorite = " + apodEntity.getIsFavorite());
        new markFavoriteAsyncTask(apodFavoritesDao).execute(apodEntity);
    }

    private static class markFavoriteAsyncTask extends AsyncTask<ApodEntity, Void, Void> {
        private ApodFavoritesDao apodFavoritesAsyncDao;

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

}
