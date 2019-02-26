package com.amiculous.nasaview.util;

import android.net.Uri;

import timber.log.Timber;

public class MiscUtils {

    private static final String YOUTUBE_IMAGE_BASE = "https://img.youtube.com/vi/";
    private static final String THUMBNAIL_FILE = "0.jpg";

    private static String getYouTubeIdFromUrl(String url) {
         Uri youTubeUri = Uri.parse(url);
        return youTubeUri.getLastPathSegment();
    }

    public static String videoThumbnailUrl(String videoUrl) {
        String videoId = getYouTubeIdFromUrl(videoUrl);
        Uri uri = Uri.parse(YOUTUBE_IMAGE_BASE).buildUpon().appendPath(videoId).appendPath(THUMBNAIL_FILE).build();
        return uri.toString();
    }
}
