package com.amiculous.nasaview.ui.apod;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import timber.log.Timber;

public class SingleApodViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application app;
    private final String date;

    public SingleApodViewModelFactory(Application app, String date) {
        Timber.i("constructing SingleApodViewModelFactory");
        this.app = app;
        this.date = date;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SingleApodViewModel(app, date);
    }
}
