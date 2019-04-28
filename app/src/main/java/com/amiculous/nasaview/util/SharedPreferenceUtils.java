package com.amiculous.nasaview.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.amiculous.nasaview.data.ApodEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.annotation.NonNull;

public class SharedPreferenceUtils {

    private static final String PREFERENCES_KEY = "nasa_view_preferences";
    private static final String TODAYS_APOD_JSON = "todays_apod_json";
    public static final String APOD_JSON_KEY = "apod_json_key";
    private static final String WANTS_HD_KEY = "wants_hd_key";

    private static Gson gson = new Gson();

    //simple helpers:
    public static String apodToJSONstring(ApodEntity apodEntity) {
        if (apodEntity != null)
            return gson.toJson(apodEntity);
        else
            return "";
    }

    @SuppressWarnings("all")
    public static ApodEntity jsonToApod(String apodJsonString) throws JsonSyntaxException {
        if (apodJsonString != null) {
            try {
                return gson.fromJson(apodJsonString, ApodEntity.class);
            } catch (JsonSyntaxException e) {
                throw e; //intentional. see usage
            }
        } else
            return null;
    }

    public static boolean isTodaysApodJsonUpToDate(Context context, String todaysDate) {
        String apodJson = fetchTodaysApodJson(context);
        ApodEntity apodEntity = jsonToApod(apodJson);
        if (apodEntity != null) {
            return apodEntity.getDate().equals(todaysDate);
        } else return false;
    }


    private static SharedPreferences getSharedPreferences (@NonNull final Context context) {
        return context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor (@NonNull final Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void storeTodaysApodJson(@NonNull final Context context, @NonNull final String apodJson){
        getSharedPreferencesEditor(context).putString(TODAYS_APOD_JSON, apodJson).apply();
    }

    public static String fetchTodaysApodJson(@NonNull final Context context){
        return getSharedPreferences(context).getString(TODAYS_APOD_JSON, null);
    }

    public static void storeWantsHD(@NonNull final Context context, boolean wantsHD){
        getSharedPreferencesEditor(context).putBoolean(WANTS_HD_KEY, wantsHD).apply();
    }

    public static boolean fetchWantsHD(@NonNull final Context context){
        return getSharedPreferences(context).getBoolean(WANTS_HD_KEY, false);
    }
}
