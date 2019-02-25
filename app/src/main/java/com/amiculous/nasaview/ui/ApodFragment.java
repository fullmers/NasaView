package com.amiculous.nasaview.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ApodFragment extends Fragment implements ApodContract.View {

    private static final String TAG = ApodFragment.class.getSimpleName();
    private ApodContract.Presenter presenter;
    private ApodEntity apodEntity;
    private boolean isFavorite;
    private FavoritesViewModel favoritesViewModel;

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.date_text) TextView dateText;
    @BindView(R.id.title_text) TextView titleText;
    @BindView(R.id.desc_text) TextView descText;
    @BindView(R.id.progress_circular) ProgressBar progressBar;
    @BindView(R.id.favorite_fab) FloatingActionButton favoritesFAB;

    public static ApodFragment newInstance() {
        return new ApodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apod_fragment, container, false);
        ButterKnife.bind(this, view);
        setPresenter();
        presenter.loadTodaysApod();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenter();
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        favoritesViewModel.getAllFavoriteApods().observe(getActivity(), new Observer<List<ApodEntity>>() {
            @Override
            public void onChanged(@Nullable List<ApodEntity> apodEntities) {
                Timber.i("onChanged");
                if(apodEntities!= null) {
                    Timber.i("apodEntities NOT null");
                for(ApodEntity apod:apodEntities) {
                    Timber.i(apod.getTitle());
                }} else {
                    Timber.i("apodEntities NULL");
                }
            }
        });
    }


    @OnClick(R.id.image)
    public void selectImage(View view) {
        presenter.openImageFullScreem(apodEntity);
    }

    @OnClick(R.id.favorite_fab)
    public void onFabTap(View view) {
        if (isFavorite) {
            //Does not work yet
            //TODO get this working
            favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border_white_24dp));
            isFavorite = false;
            favoritesViewModel.delete(apodEntity);
            Timber.i("untapping favorite button");
        } else {

            favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_white_24dp));
            isFavorite = true;
            favoritesViewModel.insert(apodEntity);
            isFavorite = true;
            Timber.i("tapping favorite button");
        }
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
        Picasso.with(getActivity().getApplicationContext())
                .load(url)
                .placeholder(getResources().getDrawable(R.drawable.default_apod))
                .into(imageView);
    }

    @Override
    public void setApod(String copyright, String date, String explanation, String mediaType, String title, String url) {
        Timber.i("setting apod");
        apodEntity = new ApodEntity(copyright, date, explanation, mediaType, title, url);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter() {
        this.presenter = new ApodPresenter(this);
    }

    @Override
    public void showImageFullScreen(Image image) {
        Timber.i("showImageFullScreen");
        Intent tempStartFavoritesIntent = new Intent(getActivity(), FavoritesActivity.class);
        startActivity(tempStartFavoritesIntent);
        //TODO open image in a full screen pinch-to-zoom view
    }

}
