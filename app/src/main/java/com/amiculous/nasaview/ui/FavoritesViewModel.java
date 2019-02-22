package com.amiculous.nasaview.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private ApodRepository apodRepository;

    private LiveData<List<ApodEntity>> allFavoriteApods;

    public FavoritesViewModel(Application application) {
        super(application);
        apodRepository = new ApodRepository(application);
        allFavoriteApods = apodRepository.getAllFavoriteApods();
    }

    LiveData<List<ApodEntity>> getAllFavoriteApods() {return allFavoriteApods;};

    public void insert(ApodEntity apodEntity) {apodRepository.insertApod(apodEntity);}

    public void delete(ApodEntity apodEntity) {apodRepository.deleteApod(apodEntity);}

}
