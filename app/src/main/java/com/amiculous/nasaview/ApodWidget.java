package com.amiculous.nasaview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.ui.MainActivity;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.google.gson.JsonSyntaxException;

public class ApodWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Tapping on the widget opens the app
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.apod_widget);
        remoteViews.setOnClickPendingIntent(R.id.widget_background, pendingIntent);

        //The widget displays the title of today's apod.
        // Refreshes from shared prefs every 30 minuts (which are updated once per day).
        String title = getTodaysApodTitle(context);
        remoteViews.setTextViewText(R.id.apod_title, title);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static String getTodaysApodTitle(Context context) {
        String fetchedApodJson = SharedPreferenceUtils.fetchTodaysApodJson(context);
        if (fetchedApodJson != null && fetchedApodJson.length() != 0) {
            try {
                ApodEntity todaysApod = SharedPreferenceUtils.jsonToApod(fetchedApodJson);
                if (todaysApod != null) {
                    return todaysApod.getTitle();
                } else {
                    return context.getString(R.string.apod_data_not_yet_available);
                }
            } catch (JsonSyntaxException e) {
                return context.getString(R.string.apod_data_not_yet_available);
            }
        } else {
            return context.getString(R.string.apod_data_not_yet_available);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}
}

