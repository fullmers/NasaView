package com.amiculous.nasaview.ui.apod;

import com.amiculous.nasaview.data.Image;
import com.amiculous.nasaview.data.MediaType;
import com.amiculous.nasaview.ui.BasePresenter;
import com.amiculous.nasaview.ui.BaseView;

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
        void showCopyright(String copyright);
        void hideCopyright();
    }

    interface Presenter extends BasePresenter {
        void openImageFullScreem(Image image);
        void loadTodaysApod();
        MediaType getMedia_type();
    }
}