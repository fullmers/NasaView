package com.amiculous.nasaview.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.MediaType;
import com.amiculous.nasaview.util.MiscUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;


public class BinderAdapaters {

    @BindingAdapter("displayApod")
    public static void configureApod(@NonNull ImageView imageView, ApodEntity apodEntity) {
        String mediaType = apodEntity.getMedia_type();
        switch (mediaType) {
            case "image":
                Picasso.get()
                        .load(apodEntity.getUrl())
                        .placeholder(imageView.getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                break;
            case "video":
                String thumbnailUrl = MiscUtils.videoThumbnailUrl(apodEntity.getUrl());
                Picasso.get()
                        .load(thumbnailUrl)
                        .placeholder(imageView.getResources().getDrawable(R.drawable.default_apod))
                        .into(imageView);
                imageView.setOnClickListener(v -> {
                    Intent launchYouTube = new Intent(Intent.ACTION_VIEW);
                    launchYouTube.setData(Uri.parse(apodEntity.getUrl()));
                    imageView.getContext().startActivity(launchYouTube);
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