package com.amiculous.nasaview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.Image;

public class ImageDetail extends AppCompatActivity {

    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            image = extras.getParcelable("IMAGE");
            Log.d("TEST",image.getTitle());
        }
    }
}
