package com.amiculous.nasaview.ui.apod;

import android.app.Application;
import android.content.Context;

import com.amiculous.nasaview.data.ApodCallback;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.data.ApodRepository;
import com.amiculous.nasaview.util.SharedPreferenceUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class SingleApodViewModel extends AndroidViewModel implements ApodCallback {

    private Context context;
    private Application application;
    public ApodEntity apodEntity;
    ApodRepository repository;

    private LiveData<ApodEntity> apodEntityLive;
    private boolean showPlayButton;

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> copyright = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    private ObservableField<String> explanation = new ObservableField<>();
    public ObservableField<Boolean> isFavorite = new ObservableField<>();
    private ObservableField<Boolean> showApod = new ObservableField<>();
    private ObservableField<Boolean> showError = new ObservableField<>();
    private ObservableField<Boolean> showProgressSpinner = new ObservableField<>();

    public SingleApodViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getBaseContext();
        this.application = application;
        setCallState(ApodRepository.ApodCallState.HAS_NOT_TRIED);
    }

    void init() {
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        boolean isApodCurrent = SharedPreferenceUtils.isTodaysApodJsonUpToDate(context, todaysDate);
        Timber.i("isApodCurrent: %s", isApodCurrent);
        if (isApodCurrent) {
            String apodString = SharedPreferenceUtils.fetchTodaysApodJson(context);
            ApodEntity apodEntity = SharedPreferenceUtils.jsonToApod(apodString);
            setViews(apodEntity);
            if (apodEntity.getMedia_type().equals("video")) {
                setShowPlayButton(true);
            } else {
                setShowPlayButton(false);
            }
            showApod();
        }
            repository = new ApodRepository(application, todaysDate, this);

        apodEntityLive = repository.getApod();
    }

    private void setViews(ApodEntity apodEntity) {
        setTitle(apodEntity.getTitle());
        setCopyright(apodEntity.getCopyright());
        setDate(apodEntity.getDate());
        setExplanation(apodEntity.getExplanation());
        setApodEntity(apodEntity);
        Timber.i("calling set Views. with isFavorite value:%s", apodEntity.getIsFavorite());
        setIsFavorite(apodEntity.getIsFavorite());
    }

    void toggleIsFavorite(ApodEntity apod, boolean isFavoriteNow) {
        Timber.i("calling toggleIsFavorite");
        boolean markAsFavorite = !isFavoriteNow;
        ApodRepository.markFavorite(apod, markAsFavorite);
        apod.setIsFavorite(markAsFavorite);
        isFavorite.set(markAsFavorite);
        SharedPreferenceUtils.storeTodaysApodJson(context, SharedPreferenceUtils.apodToJSONstring(apod));
    }

    LiveData<ApodEntity> getApodEntityLive() {
        return apodEntityLive;
    }

    @Override
    public void setCallState(ApodRepository.ApodCallState callState) {
        Timber.i("Call State:%s", callState);
        switch (callState) {
            case SUCCESSFUL: {
                showApod();
                break;
            }
            case WAITING: {
                showSpinner();
                break;
            }
            case FAILED: {
                showError();
                break;
            }
            case HAS_NOT_TRIED: {
                showSpinner();
            }
        }
    }

    private void showApod() {
        showApod.set(true);
        showError.set(false);
        showProgressSpinner.set(false);
    }

    private void showSpinner() {
        showApod.set(false);
        showError.set(false);
        showProgressSpinner.set(true);
    }

    private void showError() {
        showApod.set(false);
        showError.set(true);
        showProgressSpinner.set(false);
    }


    @Override
    public void setApod(ApodEntity apodEntity) {
        String apodString = SharedPreferenceUtils.apodToJSONstring(apodEntity);
        setViews(apodEntity);
        Timber.i("Apod Returned:%s", apodString);
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


    public ObservableField<String> getTitle() {
        return title;
    }

    public void setTitle(ObservableField<String> title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableField<String> getExplanation() {
        return explanation;
    }

    public void setExplanation(ObservableField<String> explanation) {
        this.explanation = explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation.set(explanation);
    }

    public ObservableField<String> getCopyright() {
        return copyright;
    }

    public void setCopyright(ObservableField<String> copyright) {
        this.copyright = copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright.set(copyright);
    }

    public ObservableField<String> getDate() {
        return date;
    }

    public void setDate(ObservableField<String> date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public boolean isShowPlayButton() {
        return showPlayButton;
    }

    private void setShowPlayButton(boolean showPlayButton) {
        this.showPlayButton = showPlayButton;
    }

    public ObservableField<Boolean> getShowApod() {
        return showApod;
    }

    @SuppressWarnings("unused")
    public void setShowApod(ObservableField<Boolean> showApod) {
        this.showApod = showApod;
    }

    public ObservableField<Boolean> getShowError() {
        return showError;
    }

    @SuppressWarnings("unused")
    public void setShowError(ObservableField<Boolean> showError) {
        this.showError = showError;
    }

    public ObservableField<Boolean> getShowProgressSpinner() {
        return showProgressSpinner;
    }

    @SuppressWarnings("unused")
    public void setShowProgressSpinner(ObservableField<Boolean> showProgressSpinner) {
        this.showProgressSpinner = showProgressSpinner;
    }
}

