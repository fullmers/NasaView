package com.amiculous.nasaview.ui;

import android.support.annotation.NonNull;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;

import static com.amiculous.nasaview.util.Preconditions.checkNotNull;


public class ApodPresenter implements ApodContract.Presenter {

    private static final String TAG = "APOD Presenter";
    private final ApodContract.View apodView;
    private ApodEntity apod;

    ApodPresenter(@NonNull  ApodContract.View apodView) {
        checkNotNull(apodView);
        this.apodView = apodView;
        apodView.setPresenter(this);
    }


    @Override
    public void openImageDetails(@NonNull Image image){
        checkNotNull(image);

    }

    @Override
    public void loadTodaysApod() {}

    @Override
    public void loadNewApod(boolean forceUpdate) {}

    @Override
    public void start() {}

}
