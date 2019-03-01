package com.amiculous.nasaview.ui.favorites;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.amiculous.nasaview.AppExecutors;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoritesViewModel extends AndroidViewModel {

    private ApodRepository apodRepository;

    private LiveData<List<ApodEntity>> allFavoriteApods;

    public FavoritesViewModel(Application application) {
        super(application);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        apodRepository = new ApodRepository(application, date);
        allFavoriteApods = apodRepository.getAllFavoriteApods();
    }

    LiveData<List<ApodEntity>> getAllFavoriteApods() {return allFavoriteApods;}

}
