package com.amiculous.nasaview.ui.apod;

import android.app.Application;

import com.amiculous.nasaview.data.ApodCallback;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class SingleApodViewModel extends ViewModel implements ApodCallback {

    private ApodRepository apodRepository;
    private LiveData<ApodEntity> apod;
    private MutableLiveData<Boolean> wasSuccessful = new MutableLiveData<>();

    public String getDate() {
        return date;
    }

    private String date;

    public SingleApodViewModel(Application application, String date) {
        Timber.i("constructing SingleApodViewModel %s", date);
        apodRepository = new ApodRepository(application, date, this);
        this.date = date;
        apod = apodRepository.getApod(date);
    }

    @Override
    public void wasSuccessful(boolean apodCallWasSuccessful) {
        setWasSuccessful(apodCallWasSuccessful);
        //  setWasSuccessful(false);
    }

    MutableLiveData<Boolean> getWasSuccessful() {
        return wasSuccessful;
    }

    private void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful.postValue(wasSuccessful);
    }

    public LiveData<ApodEntity> getApod() {
        Timber.i("getApod");
        apodRepository.getApod(date);
        return apod;
    }

    void markFavorite(ApodEntity apodEntity) {
        Timber.i("calling markFavorite from SingleApodViewModel. isFavorite = %s", apodEntity.getIsFavorite());
        apodRepository.markFavorite(apodEntity);
    }
}

