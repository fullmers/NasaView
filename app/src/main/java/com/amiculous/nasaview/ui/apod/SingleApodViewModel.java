package com.amiculous.nasaview.ui.apod;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.amiculous.nasaview.AppExecutors;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import timber.log.Timber;

public class SingleApodViewModel extends AndroidViewModel {

    private ApodRepository apodRepository;
    private LiveData<ApodEntity> apod;
    private String date;

    public SingleApodViewModel(Application application, String date) {
        super(application);
        Timber.i("constructing SingleApodViewModel " + date);
        apodRepository = new ApodRepository(application, date);
        this.date = date;
        apod = apodRepository.getApod(date);
    }

    public LiveData<ApodEntity> getApod() {
        Timber.i("getApod");
        return apod;
    }

    public void markFavorite(ApodEntity apodEntity) {
        Timber.i("calling markFavorite from SingleApodViewModel. isFavorite = " + apodEntity.getIsFavorite());
        apodRepository.markFavorite(apodEntity);
    }

    public String getDate() {return date;}
}

