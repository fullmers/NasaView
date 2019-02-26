package com.amiculous.nasaview.ui;

import android.support.annotation.NonNull;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.data.MediaType;

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

                apodView.hideProgressBar();
                apodView.addApodExplanation(apod.getExplanation());
                apodView.addApodDate(apod.getDate());
                apodView.addApodTitle(apod.getTitle());
                apodView.addApodImage(apod.getUrl(), getMedia_type());
                apodView.setApod(apod.getCopyright(), apod.getDate(), apod.getExplanation(), apod.getMedia_type(), apod.getTitle(), apod.getUrl());
            }

            @Override
            public void onFailure(@NonNull Call<ApodEntity> call, @NonNull Throwable t) {
                apodView.hideProgressBar();
            }
        });
    }

    private MediaType getMedia_type() {
        if (apod.getMedia_type().equals("video"))
            return MediaType.VIDEO;
        else
            return MediaType.IMAGE;
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
