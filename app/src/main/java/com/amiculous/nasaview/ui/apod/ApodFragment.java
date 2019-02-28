package com.amiculous.nasaview.ui.apod;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.api.ApodApi;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.data.MediaType;
import com.amiculous.nasaview.util.MiscUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.amiculous.nasaview.BuildConfig.API_KEY;

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

    public static ApodFragment newInstance() {
        return new ApodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.i("Calling onCreateView");
        View view = inflater.inflate(R.layout.apod_fragment, container, false);
        ButterKnife.bind(this, view);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return view;
    }

    //TODO don't reload APOD if have already gotten today's when navigating back here from other
    // bottom navigation tab
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("Calling onActivityCreated");
        SingleApodViewModelFactory factory = new SingleApodViewModelFactory(getActivity().getApplication(),date);
        singleApodViewModel = ViewModelProviders.of(getActivity(), factory).get(SingleApodViewModel.class);
        liveApod = singleApodViewModel.getApod();
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
        liveApod.observe(this, new Observer<ApodEntity>() {
            @Override
            public void onChanged(@Nullable ApodEntity apod) {
                if (apod != null) {
                    addApodTitle(apod.getTitle());
                    addApodImage(apod.getUrl(), apod.getMedia_type().equals("video") ? MediaType.VIDEO : MediaType.IMAGE);
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
    }

    public void setFABButtonToFavoriteState() {
        Timber.i("setting favorite state to true");
        isFavorite = true;
        apodEntity.setIsFavorite(true);
        singleApodViewModel.markFavorite(apodEntity);
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_white_24dp));
    }

    public void setFABButtonToUnfavoriteState() {
        Timber.i("setting favorite state to false");
        isFavorite = false;
        apodEntity.setIsFavorite(false);
        singleApodViewModel.markFavorite(apodEntity);
        favoritesFAB.setImageDrawable(getActivity().getDrawable(R.drawable.ic_star_border_white_24dp));
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
                Picasso.with(getActivity().getApplicationContext())
                        .load(url)
                        .placeholder(getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                break;
            case VIDEO:
                showPlayButton();
                String thumbnailUrl = MiscUtils.videoThumbnailUrl(url);
                Picasso.with(getActivity().getApplicationContext())
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
        //TODO determine if thumbnail is mostly light or dark and display white/black play button
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

}
