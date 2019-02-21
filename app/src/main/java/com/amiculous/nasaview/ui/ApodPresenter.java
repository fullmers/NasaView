package com.amiculous.nasaview.ui;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amiculous.nasaview.util.Preconditions.checkNotNull;


public class ApodPresenter implements ApodContract.Presenter {

    private static final String TAG = "APOD Presenter";
    private final ApodContract.View apodView;
    private ApodEntity apod;
    private static final String API_KEY = BuildConfig.API_KEY;

    public ApodPresenter(@NonNull ApodContract.View apodView) {
        checkNotNull(apodView);
        this.apodView = apodView;
    }


    @Override
    public void openImageFullScreem(Image image){
        try {
            apodView.showImageFullScreen(image);
        } catch (NullPointerException e) {
            Log.d(TAG, "image has not loaded yet");
        }
    }

    @Override
    public void loadTodaysApod() {
        ApodApi apodApi= ApodApi.retrofit.create(ApodApi.class);
        final Call<ApodEntity> call = apodApi.getApod(API_KEY);
        Log.d(TAG,call.request().url().toString());

        apodView.showProgressBar();
        call.enqueue(new Callback<ApodEntity>() {
            @Override
            public void onResponse(Call<ApodEntity> call, Response<ApodEntity> response) {
                apod = response.body();
                Log.d(TAG,apod.getExplanation());
                apodView.hideProgressBar();
                apodView.addApodExplanation(apod.getExplanation());
                apodView.addApodDate(apod.getDate());
                apodView.addApodTitle(apod.getTitle());
                apodView.addApodImage(apod.getUrl());
                apodView.setApod(apod.getCopyright(), apod.getDate(), apod.getExplanation(), apod.getMediaType(), apod.getTitle(), apod.getUrl());
            }

            @Override
            public void onFailure(@NonNull Call<ApodEntity> call, @NonNull Throwable t) {
                apodView.hideProgressBar();
                Log.d(TAG,t.getMessage());
            }
        });
    }

    @Override
    public void loadNewApod(boolean forceUpdate) {}

    @Override
    public void start() {}

}
