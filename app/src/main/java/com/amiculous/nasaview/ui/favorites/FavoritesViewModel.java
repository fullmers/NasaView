package com.amiculous.nasaview.ui.favorites;

import android.app.Application;

import com.amiculous.nasaview.data.ApodCallback;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoritesViewModel extends AndroidViewModel implements ApodCallback {

    private final LiveData<List<ApodEntity>> allFavoriteApods;

    public FavoritesViewModel(Application application) {
        super(application);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ApodRepository apodRepository = new ApodRepository(application, date, this);
        allFavoriteApods = apodRepository.getAllFavoriteApods();
    }

    LiveData<List<ApodEntity>> getAllFavoriteApods() {return allFavoriteApods;}

    @Override
    public void setCallState(ApodRepository.ApodCallState callState) {

    }

    @Override
    public void setApod(ApodEntity apodEntity) {

    }
}
