package com.amiculous.nasaview.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.NonNull;

public class FirebaseUtils {
    public static final String FAVORITES_FRAGMENT = "favorites_fragment";
    public static final String APOD_FRAGMENT = "apod_fragment";
    public static final String SEARCH_FRAGMENT = "search_fragment";

    private static FirebaseAnalytics analytics;

    public static void init(Context context) {
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public static void screenShown(Activity activity, @NonNull String screenName) {
        if (activity != null) {
            analytics.setCurrentScreen(activity, screenName, null);
        }
    }

    public static void logEvent(String firebaseEvent, Bundle bundle) {
        analytics.logEvent(firebaseEvent, bundle);
    }
}
