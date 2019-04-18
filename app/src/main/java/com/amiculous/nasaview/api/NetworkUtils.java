package com.amiculous.nasaview.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.data.ApodEntity;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import timber.log.Timber;

public class NetworkUtils {
    private static final Gson gson = new Gson();
    private static final String API_KEY_VALUE = BuildConfig.API_KEY;
    private static final String API_KEY_LABEL = "api_key";

    @SuppressWarnings("CaughtExceptionImmediatelyRethrown")
    public static ApodEntity jsonToApod(String apodJsonString) throws JsonSyntaxException {
        if (apodJsonString != null) {
            try {
                return gson.fromJson(apodJsonString, ApodEntity.class);
            } catch (JsonSyntaxException e) {
                throw e;
            }
        } else
            return null;
    }

    public static URL buildUrl() {
        Uri apodQueryUri;
        String BASE_URL = "https://api.nasa.gov/planetary/apod";
        apodQueryUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_LABEL, API_KEY_VALUE)
                    .build();
        try {
            URL apodQueryUrl = new URL(apodQueryUri.toString());
            Timber.i(apodQueryUrl.toString());
            return apodQueryUrl;
        } catch (MalformedURLException e) {
            Crashlytics.logException(e);
            return null;
        }
    }

    //from the Sunshine App
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            Crashlytics.logException(new IOException());
            urlConnection.disconnect();
        }
    }

    @SuppressWarnings("unused")
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
