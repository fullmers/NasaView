package com.amiculous.nasaview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.ui.ApodContract;
import com.amiculous.nasaview.ui.ApodPresenter;

import static com.amiculous.nasaview.util.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements ApodContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ApodContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPresenter();
        presenter.loadTodaysApod();
    }

    @Override
    public void addApodExplanation(String explanation) {
        Log.d(TAG,explanation);
    }

    @Override
    public void setPresenter() {
        this.presenter = new ApodPresenter(this);
    }
}
