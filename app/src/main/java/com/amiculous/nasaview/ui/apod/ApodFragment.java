package com.amiculous.nasaview.ui.apod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.MediaType;
import com.amiculous.nasaview.util.MiscUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ApodFragment extends Fragment {

    private ApodEntity apodEntity;
    private boolean isFavorite;
    private SingleApodViewModel singleApodViewModel;
    private String date;
    private Unbinder unbinder;
    private Activity activity;

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.play_button)
    ImageView playButton;
    @BindView(R.id.date_text)
    TextView dateText;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.desc_text)
    TextView descText;
    @BindView(R.id.copyright_text)
    TextView copyrightText;
    @BindView(R.id.copyright_layout)
    LinearLayout copyrightLayout;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    @BindView(R.id.favorite_fab)
    FloatingActionButton favoritesFAB;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollView;
    @BindView(R.id.error_layout) LinearLayout errorLayout;

    public static ApodFragment newInstance() {
        return new ApodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apod, container, false);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        singleApodViewModel = ViewModelProviders.of(this).get(SingleApodViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        singleApodViewModel.initFields(date);
        populateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        singleApodViewModel.getWasSuccessful().removeObservers(this);
        singleApodViewModel.getApod().removeObservers(this);
    }

    private void populateUI() {
        LiveData<ApodEntity> liveApod = singleApodViewModel.getApod();

        singleApodViewModel.getWasSuccessful().observe(this, wasSuccessful -> {
            if (wasSuccessful) {
                showApodHideError();
            } else {
                hideApodShowError();
            }
        });

        liveApod.observe(getViewLifecycleOwner(), apod -> {
            if (apod != null) {
                showApodHideError();
                apodEntity = apod;
                addApodTitle(apod.getTitle());
                addApodImage(apod.getUrl(), apod.getMedia_type().equals("video") ? MediaType.VIDEO : MediaType.IMAGE);
                addApodDate(apod.getDate());
                addApodExplanation(apod.getExplanation());

                isFavorite = apod.getIsFavorite();
                if (isFavorite)
                    setFABButtonToFavoriteState();
                else
                    setFABButtonToUnfavoriteState();

                if(apod.getCopyright() == null)
                    hideCopyright();
                else
                    showCopyright(apod.getCopyright());
            } else {
                hideApodShowError();
            }
        });
    }

    @OnClick(R.id.image)
    void selectImage() {
        //TODO open image in a full screen pinch-to-zoom view
    }

    @OnClick(R.id.favorite_fab)
    void onFabTap() {
        if (isFavorite) {
            setFABButtonToUnfavoriteState();
        } else {
            setFABButtonToFavoriteState();
        }
        updateFavoriteStatus();
    }

    //todo refactor below with databinding
    private void setFABButtonToFavoriteState() {
        isFavorite = true;
        favoritesFAB.setImageDrawable(activity.getDrawable(R.drawable.ic_star_white_24dp));
    }

    private void setFABButtonToUnfavoriteState() {
        isFavorite = false;
        favoritesFAB.setImageDrawable(activity.getDrawable(R.drawable.ic_star_border_white_24dp));
    }

    private void updateFavoriteStatus() {
        apodEntity.setIsFavorite(isFavorite);
        singleApodViewModel.markFavorite(apodEntity, isFavorite);
    }

    private void addApodExplanation(String description) {
        descText.setText(description);
    }

    private void addApodDate(String date) {
        dateText.setText(date);
    }

    private void addApodTitle(String title) {
        titleText.setText(title);
    }

    private void addApodImage(final String url, MediaType mediaType) {
        switch (mediaType) {
            case IMAGE:
                hidePlayButton();
                Picasso.get()
                        .load(url)
                        .placeholder(getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                break;
            case VIDEO:
                showPlayButton();
                String thumbnailUrl = MiscUtils.videoThumbnailUrl(url);
                Picasso.get()
                        .load(thumbnailUrl)
                        .placeholder(getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                imageView.setOnClickListener(v -> {
                    Intent launchYouTube = new Intent(Intent.ACTION_VIEW);
                    launchYouTube.setData(Uri.parse(url));
                    startActivity(launchYouTube);
                });
        }
    }

    private void showPlayButton() {
        playButton.setVisibility(View.VISIBLE);
    }

    private void hidePlayButton() {
        playButton.setVisibility(View.INVISIBLE);
    }

    private void showCopyright(String copyright) {
        copyrightLayout.setVisibility(View.VISIBLE);
        copyrightText.setText(copyright);
    }

    private void hideCopyright() {
        copyrightLayout.setVisibility(View.GONE);
    }

    private void showApodHideError() {
        appBarLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        favoritesFAB.show();
        errorLayout.setVisibility(View.GONE);
    }

    private void hideApodShowError() {
        appBarLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        favoritesFAB.hide();
        errorLayout.setVisibility(View.VISIBLE);
    }

}
