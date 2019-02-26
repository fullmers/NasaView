package com.amiculous.nasaview.ui.apod;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class SingleApodViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application app;
    private final String date;

    public SingleApodViewModelFactory(Application app, String date) {
        this.app = app;
        this.date = date;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SingleApodViewModel(app, date);
    }
}
