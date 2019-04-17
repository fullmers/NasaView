package com.amiculous.nasaview.ui.apod;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ApodFragment extends Fragment {

    private ApodEntity apodEntity;
    private boolean isFavorite;
    private SingleApodViewModel singleApodViewModel;
    private LiveData<ApodEntity> liveApod;
    private String date;

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
        Timber.i("Calling onCreateView");
        View view = inflater.inflate(R.layout.fragment_apod, container, false);
        ButterKnife.bind(this, view);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("Calling onActivityCreated");
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
        SingleApodViewModelFactory factory = new SingleApodViewModelFactory(getActivity().getApplication(),date);
        singleApodViewModel = ViewModelProviders.of(getActivity(), factory).get(SingleApodViewModel.class);

        liveApod = singleApodViewModel.getApod();
        liveApod.observe(getViewLifecycleOwner(), new Observer<ApodEntity>() {
            @Override
            public void onChanged(@Nullable ApodEntity apod) {
                Timber.i("calling onChanged");
                if (apod != null) {
                    apodEntity = apod;
                    Timber.i("apod was not null");
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
                } else
                    Timber.i("apod WAS null");
            }
        });

        singleApodViewModel.getWasSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wasSuccessful) {
                if (wasSuccessful) {
                    Timber.i("apod call successful");
                    showApodHideError();
                } else {
                    hideApodShowError();
                    Timber.i("apod call FAILED");
                }
            }
        });


    }

    @OnClick(R.id.image)
    public void selectImage(View view) {
        //TODO open image in a full screen pinch-to-zoom view
    }

    @OnClick(R.id.favorite_fab)
    public void onFabTap(View view) {
        if (isFavorite) {
            setFABButtonToUnfavoriteState();
        } else {
            setFABButtonToFavoriteState();
        }
        updateFavoriteStatus();
    }

    public void setFABButtonToFavoriteState() {
        Timber.i("setting favorite state to true");
        isFavorite = true;
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_white_24dp));
    }

    public void setFABButtonToUnfavoriteState() {
        Timber.i("setting favorite state to false");
        isFavorite = false;
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_border_white_24dp));
    }

    private void updateFavoriteStatus() {
        apodEntity.setIsFavorite(isFavorite);
        singleApodViewModel.markFavorite(apodEntity);
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
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchYouTube = new Intent(Intent.ACTION_VIEW);
                        launchYouTube.setData(Uri.parse(url));
                        startActivity(launchYouTube);
                    }
                });
        }
    }

    private void showPlayButton() {
        playButton.setVisibility(View.VISIBLE);
    }

    private void hidePlayButton() {
        playButton.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
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
