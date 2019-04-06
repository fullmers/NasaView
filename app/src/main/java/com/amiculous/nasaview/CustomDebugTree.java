package com.amiculous.nasaview;

import android.util.Log;

//import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class CustomDebugTree extends Timber.DebugTree {
    private static final int MAX_LOG_LENGTH = 4000;
    private static final String MY_TAG = "MyDebugger ";

    @Override protected void log(int priority, String tag, String message, Throwable t) {
        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(MY_TAG + tag, message);
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
                    Log.wtf(MY_TAG + tag, part);
                } else {
                    Log.println(priority, MY_TAG, part);
                }
                i = end;
            } while (i < newline);
        }
    }
}
