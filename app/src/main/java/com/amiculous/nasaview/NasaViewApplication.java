package com.amiculous.nasaview;

import android.app.Application;

import com.amiculous.nasaview.util.FirebaseUtils;
import com.amiculous.nasaview.workers.DeleteNonFavoriteApodsWorker;
import com.amiculous.nasaview.workers.FetchApodWorker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
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

        initWorkers();
    }

    private void initWorkers() {
        Constraints constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest updateWidgetWorkRequest = new PeriodicWorkRequest
                .Builder(FetchApodWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraint)
                .build();

        PeriodicWorkRequest cleanupDbWorkRequest = new PeriodicWorkRequest
                .Builder(DeleteNonFavoriteApodsWorker.class, 30, TimeUnit.DAYS)
                .setConstraints(constraint)
                .build();

        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(updateWidgetWorkRequest);
        workManager.enqueue(cleanupDbWorkRequest);
    }
}
