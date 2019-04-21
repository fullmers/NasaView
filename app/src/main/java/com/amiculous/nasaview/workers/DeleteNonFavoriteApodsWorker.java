package com.amiculous.nasaview.workers;

import android.content.Context;
import com.amiculous.nasaview.data.ApodFavoritesDao;
import com.amiculous.nasaview.data.AppDatabase;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

//clean out apods that were not marked as favorite from app db
public class DeleteNonFavoriteApodsWorker extends Worker {

    private Context context;

    public DeleteNonFavoriteApodsWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            AppDatabase db = AppDatabase.getInstance(context);
            ApodFavoritesDao apodFavoritesDao = db.apodFavoritesDao();
            apodFavoritesDao.deleteNonFavoriteApods();
            return  Result.failure();
        } catch (Throwable throwable){
            return  Result.failure();
        }
    }
}
