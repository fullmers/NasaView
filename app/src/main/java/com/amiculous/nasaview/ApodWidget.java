package com.amiculous.nasaview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.databinding.tool.util.StringUtils;
import android.widget.RemoteViews;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.ui.MainActivity;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.google.gson.JsonSyntaxException;

import androidx.room.util.StringUtil;

/**
 * Implementation of App Widget functionality.
 */
public class ApodWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.apod_widget);
        remoteViews.setOnClickPendingIntent(R.id.widget_background, pendingIntent);

        String title = getTodaysApodTitle(context);
        remoteViews.setTextViewText(R.id.apod_title, title);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static String getTodaysApodTitle(Context context) {
    /*    final String explanation = "Magnificent spiral galaxy NGC 4565 is viewed edge-on from planet Earth. Also known as the Needle Galaxy for its narrow profile, bright NGC 4565 is a stop on many telescopic tours of the northern sky, in the faint but well-groomed constellation Coma Berenices. This sharp, colorful image reveals the galaxy's bulging central core cut by obscuring dust lanes that lace NGC 4565's thin galactic plane. An assortment of other background galaxies is included in the pretty field of view, with neighboring galaxy NGC 4562 at the upper right. NGC 4565 itself lies about 40 million light-years distant and spans some 100,000 light-years.  Easily spotted with small telescopes, sky enthusiasts consider NGC 4565 to be a prominent celestial masterpiece Messier missed.";
        final ApodEntity apod = new ApodEntity("Christoph Kaltseis","2019-02-22",explanation,"image","NGC 4565: Galaxy on Edge","https://apod.nasa.gov/apod/image/1902/N4565ps06d_35tp_Kaltseis2019_1024.jpg");
        String apodJson = SharedPreferenceUtils.apodToJSONstring(apod);
        SharedPreferenceUtils.storeTodaysApodJson(context, apodJson);*/

        String fetchedApodJson = SharedPreferenceUtils.fetchTodaysApodJson(context);
        if (StringUtils.isNotBlank(fetchedApodJson)) {
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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

