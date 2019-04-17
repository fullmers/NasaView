package com.amiculous.nasaview.ui.apod;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.amiculous.nasaview.AppExecutors;
import com.amiculous.nasaview.data.ApodCallback;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import timber.log.Timber;

public class SingleApodViewModel extends AndroidViewModel implements ApodCallback {

    private ApodRepository apodRepository;
    private LiveData<ApodEntity> apod;
    private String date;

    public SingleApodViewModel(Application application, String date) {
        super(application);
        Timber.i("constructing SingleApodViewModel " + date);
        apodRepository = new ApodRepository(application, date, this);
        this.date = date;
        apod = apodRepository.getApod(date);
    }

    public LiveData<ApodEntity> getApod() {
        Timber.i("getApod");
        //TODO Find answer:
        //the following line is required, even though I don't think it should be. Why is it needed???
        apodRepository.getApod(date);
        return apod;
    }

    public void markFavorite(ApodEntity apodEntity) {
        Timber.i("calling markFavorite from SingleApodViewModel. isFavorite = " + apodEntity.getIsFavorite());
        apodRepository.markFavorite(apodEntity);
    }

    public String getDate() {return date;}

    @Override
    public boolean wasSuccessful(boolean apodCallWasSuccessful) {
        Timber.i("ApodRefresh wasSuccessful:%s", apodCallWasSuccessful);
        return apodCallWasSuccessful;
    }
}

