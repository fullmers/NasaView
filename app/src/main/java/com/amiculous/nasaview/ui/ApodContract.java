package com.amiculous.nasaview.ui;

import android.support.annotation.NonNull;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.data.MediaType;

import java.util.List;

public interface ApodContract {

    interface View extends BaseView {
        void addApodImage(String imageUrl, MediaType mediaType);
        void addApodTitle(String title);
        void addApodDate(String date);
        void addApodExplanation(String explanation);
        void setApod(String copyright, String date, String explanation, String mediaType, String title, String url);
        void showImageFullScreen(Image image);
        void showProgressBar();
        void hideProgressBar();
        void showPlayButton();
        void hidePlayButton();
    }

    interface Presenter extends BasePresenter {
        void openImageFullScreem(Image image);
        void loadTodaysApod();
        void loadNewApod(boolean forceUpdate);
        void addFavoriteApod(ApodEntity apod);
        void removeFavoriteApod(ApodEntity apod);
    }
}