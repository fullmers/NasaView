package com.amiculous.nasaview;

import android.util.Log;

import androidx.annotation.NonNull;
import timber.log.Timber;

//referred to:
//https://android.jlelse.eu/my-timber-setup-493a8ec7a10c

class CustomDebugTree extends Timber.DebugTree {
    private static final int MAX_LOG_LENGTH = 4000;
    private static final String MY_TAG = "MyDebugger ";

    @Override protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Timber.tag(MY_TAG + tag).wtf(message);
            } else {
                Log.println(priority, MY_TAG + tag, message);
            }
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Timber.tag(MY_TAG + tag).wtf(part);
                } else {
                    Log.println(priority, MY_TAG, part);
                }
                i = end;
            } while (i < newline);
        }
    }
}
