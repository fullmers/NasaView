package com.amiculous.nasaview;

import android.app.Application;

import com.amiculous.nasaview.util.FirebaseUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

@SuppressWarnings("unused")
class NasaViewApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new CustomDebugTree());
        }
        FirebaseUtils.init(this);
        FirebaseUtils.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
    }
}
