package com.amiculous.nasaview.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.Image;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ApodContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ApodContract.Presenter presenter;

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.date_text) TextView dateText;
    @BindView(R.id.title_text) TextView titleText;
    @BindView(R.id.desc_text) TextView descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setPresenter();
        presenter.loadTodaysApod();
    }

    @OnClick(R.id.image)
    public void selectImage(View view) {
        presenter.openImageDetails();
    }

    @Override
    public void addApodExplanation(String description) {
        descText.setText(description);
    }

    @Override
    public void addApodDate(String date) {
        dateText.setText(date);
    }

    @Override
    public void addApodTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void addApodImage(String url) {
        Picasso.with(this)
                .load(url)
                .placeholder(getResources().getDrawable(R.drawable.default_apod))
                .into(imageView);
    }

    @Override
    public void setPresenter() {
        this.presenter = new ApodPresenter(this);
    }

    @Override
    public void showImageDetails(Image image) {
        Log.d(TAG,"showImageDetails");
        Log.d(TAG,image.getTitle());
        Intent imageDetailIntent = new Intent(MainActivity.this,ImageDetail.class);
        //imageDetailIntent.putExtra(getString(R.string.image_extra_key),image);
        startActivity(imageDetailIntent);
    }
}
