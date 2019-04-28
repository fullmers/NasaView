package com.amiculous.nasaview.workers;

import android.content.Context;

import com.amiculous.nasaview.api.NetworkUtils;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchApodWorker extends Worker {

    private Context context;

    public FetchApodWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        context = appContext;
    }

    //call apod api and put today's apod in shared prefs
    @NonNull
    @Override
    public Result doWork() {
        try {
            boolean wantsHd = SharedPreferenceUtils.fetchWantsHD(context);
            URL ApodUrl = NetworkUtils.buildUrl(wantsHd);
            if (ApodUrl != null) {
                try {
                    String response = NetworkUtils.getResponseFromHttpUrl(ApodUrl);
                    try {
                        ApodEntity todaysApod = NetworkUtils.jsonToApod(response);
                        String apodString = SharedPreferenceUtils.apodToJSONstring(todaysApod);
                        SharedPreferenceUtils.storeTodaysApodJson(context,apodString);
                    } catch (JsonSyntaxException e) {
                        Crashlytics.logException(e);
                        return Result.failure();
                    }
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    return  Result.failure();
                }
            } else {
                return  Result.failure();
            }
            return Result.success();
        } catch (Throwable throwable){
            return  Result.failure();
        }
    }
}
