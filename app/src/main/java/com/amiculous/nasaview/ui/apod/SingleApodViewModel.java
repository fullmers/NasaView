package com.amiculous.nasaview.ui.apod;

import android.app.Application;

import com.amiculous.nasaview.data.ApodCallback;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;
//import androidx.databinding.ObservableField;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class SingleApodViewModel extends ViewModel implements ApodCallback {

    private ApodRepository apodRepository;
    private LiveData<ApodEntity> apod;
    private String date;
   // private ObservableField<Boolean>

    public SingleApodViewModel(Application application, String date) {
      //  super(application);
        Timber.i("constructing SingleApodViewModel " + date);
        apodRepository = new ApodRepository(application, date, this);
      //  this.date = date;
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

