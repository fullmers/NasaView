package com.amiculous.nasaview.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.util.MiscUtils;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class BinderAdapaters {

    @BindingAdapter("displayApod")
    public static void configureApod(@NonNull ImageView imageView, ApodEntity apodEntity) {
        String mediaType = apodEntity.getMedia_type();
        switch (mediaType) {
            case "image": {
                boolean wantsHd = SharedPreferenceUtils.fetchWantsHD(imageView.getContext());
                String url = wantsHd ? apodEntity.getHdUrl() : apodEntity.getUrl();
                Picasso.get()
                        .load(url)
                        .placeholder(imageView.getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
            }
                break;
            case "video":
                String thumbnailUrl = MiscUtils.videoThumbnailUrl(apodEntity.getUrl());
                Picasso.get()
                        .load(thumbnailUrl)
                        .placeholder(imageView.getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                imageView.setOnClickListener(v -> {
                    Intent launchYouTube = new Intent(Intent.ACTION_VIEW);
                    String url; //sometimes the api guys forget the required https: in the youtube url...
                    if (apodEntity.getUrl().startsWith("https:")) {
                        url = apodEntity.getUrl();
                    } else {
                        url = "https:" + apodEntity.getUrl();
                    }
                    if (launchYouTube.resolveActivity(imageView.getContext().getPackageManager()) != null) {
                        launchYouTube.setData(Uri.parse(url));
//                        launchYouTube.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        imageView.getContext().startActivity(launchYouTube);
                    }
                });
        }
    }

    @BindingAdapter("visibleIf")
    public static void showPlayButton(@NonNull View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter("setFavoriteButtonState")
    public static void toggleFABstate(@NonNull FloatingActionButton fab, boolean isFavorite) {
        int drawableId = R.drawable.ic_star_border_white_24dp;
        if (isFavorite) {
            drawableId = R.drawable.ic_star_white_24dp;
        }
        fab.setImageDrawable(fab.getContext().getDrawable(drawableId));
    }
}