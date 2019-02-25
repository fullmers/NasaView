package com.amiculous.nasaview;

import android.app.Application;

import timber.log.Timber;

public class NasaViewApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
}
