package com.amiculous.nasaview.ui;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodFavoritesDao;
import com.amiculous.nasaview.data.AppDatabase;
import com.amiculous.nasaview.data.Image;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.amiculous.nasaview.util.Preconditions.checkNotNull;


public class ApodPresenter implements ApodContract.Presenter {

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
            Timber.i("image has not loaded yet");
        }
    }

    @Override
    public void loadTodaysApod() {
        ApodApi apodApi= ApodApi.retrofit.create(ApodApi.class);
        final Call<ApodEntity> call = apodApi.getApod(API_KEY);
        Timber.i(call.request().url().toString());

        apodView.showProgressBar();
        call.enqueue(new Callback<ApodEntity>() {
            @Override
            public void onResponse(Call<ApodEntity> call, Response<ApodEntity> response) {
                apod = response.body();
                Timber.i(apod.getExplanation());
                Timber.i(apod.getDate());
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
            }
        });
    }

    @Override
    public void loadNewApod(boolean forceUpdate) {}

    @Override
    public void addFavoriteApod(ApodEntity apod) {
        Timber.i("calling addFavoriteApod");
        Timber.i("recieved " + apod.getTitle());
    }

    @Override
    public void removeFavoriteApod(ApodEntity apod) {}

    @Override
    public void start() {}

}
