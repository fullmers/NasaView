package com.amiculous.nasaview.ui.favorite_details;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;

public class FavoriteApodViewModel extends ViewModel {

    public String title;
    public String copyright;
    public String date;
    public String explanation;
    public  ApodEntity apodEntity;
    private String media_type;
    private String url;

    public boolean showPlayButton;

    public ObservableField<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(ObservableField<Boolean> isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite.set(isFavorite);
    }

    public ObservableField<Boolean> isFavorite = new ObservableField<Boolean>();

    public void init(ApodEntity apodEntity) {
        setTitle(apodEntity.getTitle());
        setCopyright(apodEntity.getCopyright());
        setDate(apodEntity.getDate());
        setExplanation(apodEntity.getExplanation());
        setApodEntity(apodEntity);
        setIsFavorite(apodEntity.getIsFavorite());

        if (apodEntity.getMedia_type().equals("video")) {
            setShowPlayButton(true);
        } else {
            setShowPlayButton(false);
        }
    }

    public void toggleIsFavorite(boolean isFavoriteNow) {
        ApodRepository.markFavorite(getApodEntity());
        if (isFavoriteNow) {
            isFavorite.set(false);
        } else {
            isFavorite.set(true);
        }
    }

    //Getters and setters required for data binding:

    public ApodEntity getApodEntity() {
        return apodEntity;
    }

    public void setApodEntity(ApodEntity apodEntity) {
        this.apodEntity = apodEntity;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowPlayButton() {
        return showPlayButton;
    }

    public void setShowPlayButton(boolean showPlayButton) {
        this.showPlayButton = showPlayButton;
    }

}
