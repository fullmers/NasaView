package com.amiculous.nasaview;

import android.app.Application;

import timber.log.Timber;

@SuppressWarnings("unused")
class NasaViewApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new CustomDebugTree());
    }
}
