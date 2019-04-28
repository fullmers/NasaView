package com.amiculous.nasaview.ui.settings;


import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

@SuppressWarnings("WeakerAccess") //needs to be public for androidx
public class SettingsViewModel extends ViewModel {

    public MutableLiveData<Boolean> getWantsHd() {
        return wantsHd;
    }

    public void setWantsHd(MutableLiveData<Boolean> wantsHd) {
        this.wantsHd = wantsHd;
    }

    MutableLiveData<Boolean> wantsHd = new MutableLiveData<>();


}
