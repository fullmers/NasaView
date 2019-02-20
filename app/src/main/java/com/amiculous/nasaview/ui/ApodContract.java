package com.amiculous.nasaview.ui;

import android.support.annotation.NonNull;

import com.amiculous.nasaview.data.Image;
import java.util.List;

public interface ApodContract {

    interface View extends BaseView {
        void addApodImage(String imageUrl);
        void addApodTitle(String title);
        void addApodDate(String date);
        void addApodExplanation(String explanation);
        void setApod(String copyright, String date, String explanation, String mediaType, String title, String url);
        void showImageDetails(Image image);
    }

    interface Presenter extends BasePresenter {
        void openImageDetails(Image image);
        void loadTodaysApod();
        void loadNewApod(boolean forceUpdate);
    }
}