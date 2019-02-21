package com.amiculous.nasaview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amiculous.nasaview.R;

public class ApodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apod_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ApodFragment.newInstance())
                    .commitNow();
        }
    }
}
