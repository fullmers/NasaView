package com.amiculous.nasaview.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import timber.log.Timber;

public class SingleApodViewModel extends AndroidViewModel {

    private ApodRepository apodRepository;
    private LiveData<ApodEntity> apod;
    private String date;

    public SingleApodViewModel(Application application, String date) {
        super(application);
        apodRepository = new ApodRepository(application);
        this.date = date;
        if (apodRepository.getApod(date) == null) {
            apod = null;
        } else {
            apod = apodRepository.getApod(date);
        }
    }

    public LiveData<ApodEntity> getApod() {
        return apod;
    }

    public String getDate() {return date;}

    public void insert(ApodEntity apodEntity) {apodRepository.insertApod(apodEntity);}

    public void delete(String date) {apodRepository.deleteApod(date);}

}

