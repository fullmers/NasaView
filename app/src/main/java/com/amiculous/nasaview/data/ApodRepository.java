package com.amiculous.nasaview.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.ui.apod.MyCallback;
import com.amiculous.nasaview.ui.apod.SingleApodViewModel;
import com.amiculous.nasaview.ui.apod.SingleApodViewModelFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.amiculous.nasaview.BuildConfig.API_KEY;

public class ApodRepository {

    private ApodFavoritesDao apodFavoritesDao;
    private LiveData<List<ApodEntity>> allFavoriteApods;
    private LiveData<ApodEntity> apod;
    private Executor executor;

    public ApodRepository(Application application, Executor executor, String date) {
        AppDatabase db = AppDatabase.getInstance(application);
        apodFavoritesDao = db.apodFavoritesDao();
        allFavoriteApods = apodFavoritesDao.loadAllFavoriteApods();
        this.executor = executor;
        apod = apodFavoritesDao.loadApod(date);
    }

    public LiveData<ApodEntity> getApod(final String date) {
        Timber.i("calling getApod in repositroy");
        refreshApod(date);
        return apod;
    }

    private void refreshApod(final String date) {
        Timber.i("calling refreshApod in repositroy");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Timber.i("running on executor thread");
                if (!apodFavoritesDao.hasApod(date)) {
                    Timber.i("apod was not in db");
                    ApodApi apodApi = ApodApi.retrofit.create(ApodApi.class);
                    final Call<ApodEntity> call = apodApi.getApod(API_KEY);
                    Timber.i(call.request().url().toString());
                    call.enqueue(new Callback<ApodEntity>() {
                        @Override
                        public void onResponse(Call<ApodEntity> call, Response<ApodEntity> response) {
                            ApodEntity todaysApod = response.body();
                            apodFavoritesDao.insertApod(todaysApod);
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApodEntity> call, @NonNull Throwable t) {}
                    });
                } else {
                    Timber.i("apod WAS in db");
                }
            }
        });
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
