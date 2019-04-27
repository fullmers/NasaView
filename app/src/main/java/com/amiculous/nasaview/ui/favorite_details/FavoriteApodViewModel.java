package com.amiculous.nasaview.ui.favorite_details;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;
import com.amiculous.nasaview.util.SharedPreferenceUtils;

public class FavoriteApodViewModel extends AndroidViewModel{

    public String title;
    public String copyright;
    public String date;
    private String explanation;
    public  ApodEntity apodEntity;
    public Context context;
    private boolean showPlayButton;

    public ObservableField<Boolean> isFavorite = new ObservableField<>();

    public FavoriteApodViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getBaseContext();
    }

    void init(ApodEntity apodEntity) {
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
        boolean markAsFavorite = !isFavoriteNow;
        ApodRepository.markFavorite(getApodEntity(), markAsFavorite);
        apodEntity.setIsFavorite(markAsFavorite);
        isFavorite.set(markAsFavorite);
        SharedPreferenceUtils.storeTodaysApodJson(context,SharedPreferenceUtils.apodToJSONstring(apodEntity));
    }

    //Getters and setters required for data binding:

    public ObservableField<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(ObservableField<Boolean> isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite.set(isFavorite);
    }

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

    private void setExplanation(String explanation) {
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

    private void setShowPlayButton(boolean showPlayButton) {
        this.showPlayButton = showPlayButton;
    }

}
