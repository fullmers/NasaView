package com.amiculous.nasaview.ui.apod;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.databinding.FragmentApodBinding;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ApodFragment extends Fragment {

    private ApodEntity apodEntity;
    private boolean isFavorite;
    private SingleApodViewModel singleApodViewModel;
    private Unbinder unbinder;

    public static ApodFragment newInstance() {
        return new ApodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        singleApodViewModel = ViewModelProviders.of(this).get(SingleApodViewModel.class);
        FragmentApodBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apod, container, false);
        binding.setViewmodel(singleApodViewModel);
        singleApodViewModel.init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        unbinder = ButterKnife.bind(this, view);

        LiveData<ApodEntity> liveApod = singleApodViewModel.getApodEntityLive();
        liveApod.observe(getViewLifecycleOwner(), apod -> {
            if (apod != null) {
                apodEntity = apod;
                isFavorite = apod.getIsFavorite();
            //    apodEntity.setIsFavorite(isFavorite);
            //    singleApodViewModel.toggleIsFavorite(apodEntity,isFavorite);
            }
        });
    }

    @OnClick(R.id.favorite_fab)
    void onFabTap() {
        //  apodEntity.setIsFavorite(isFavorite);
        Timber.i("calling on Fab tap");
        singleApodViewModel.toggleIsFavorite(apodEntity,isFavorite);
    }

    @OnClick(R.id.image)
    void onTap(ImageView imageView) {
        Intent launchYouTube = new Intent(Intent.ACTION_VIEW);
        if (launchYouTube.resolveActivity(imageView.getContext().getPackageManager()) != null) {
            Timber.i("package manager not null. url:" + apodEntity.getUrl());
            launchYouTube.setData(Uri.parse(apodEntity.getUrl()));
            startActivity(launchYouTube);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        singleApodViewModel.getApodEntityLive().removeObservers(this);
    }
}
