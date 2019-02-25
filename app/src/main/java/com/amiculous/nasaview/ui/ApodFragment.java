package com.amiculous.nasaview.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.data.SingleApodViewModel;
import com.amiculous.nasaview.data.SingleApodViewModelFactory;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ApodFragment extends Fragment implements ApodContract.View {

    private static final String TAG = ApodFragment.class.getSimpleName();
    private ApodContract.Presenter presenter;
    private ApodEntity apodEntity;
    private boolean isFavorite;
    private SingleApodViewModel singleApodViewModel;
    private LiveData<ApodEntity> liveApod;

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
        Timber.i("Calling onActivityCreated");
        setPresenter();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SingleApodViewModelFactory factory = new SingleApodViewModelFactory(getActivity().getApplication(),date);
        singleApodViewModel = ViewModelProviders.of(getActivity(), factory).get(SingleApodViewModel.class);
        populateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("Calling onResume");
        populateUI();
    }

    public void populateUI() {
        Timber.i("populateUI()");
        if(singleApodViewModel.getApod().getValue() != null ) {
            liveApod = singleApodViewModel.getApod();
            liveApod.observe(this, new Observer<ApodEntity>() {
                @Override
                public void onChanged(@Nullable ApodEntity apod) {
                    if (liveApod != null) {
                        setFABButtonToFavoriteState();
                    } else {
                        setFABButtonToUnfavoriteState();
                    }
                }
            });
        }
    }

    @OnClick(R.id.image)
    public void selectImage(View view) {
        presenter.openImageFullScreem(apodEntity);
    }

    @OnClick(R.id.favorite_fab)
    public void onFabTap(View view) {
        //TODO clean this up
        if (isFavorite) {
            setFABButtonToUnfavoriteState();
        } else {
            setFABButtonToFavoriteState();
        }
    }

    public void setFABButtonToFavoriteState() {
        Timber.i("setting favorite state to true");
        isFavorite = true;
        singleApodViewModel.insert(apodEntity);
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_white_24dp));
    }

    public void setFABButtonToUnfavoriteState() {
        Timber.i("setting favorite state to false");
        isFavorite = false;
        singleApodViewModel.delete(apodEntity.getDate());
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border_white_24dp));
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
        Intent tempStartFavoritesIntent = new Intent(getActivity(), FavoritesActivity.class);
        startActivity(tempStartFavoritesIntent);
        //TODO open image in a full screen pinch-to-zoom view
    }

}
