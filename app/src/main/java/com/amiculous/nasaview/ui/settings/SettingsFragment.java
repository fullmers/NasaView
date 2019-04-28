package com.amiculous.nasaview.ui.settings;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.amiculous.nasaview.BuildConfig;
import com.amiculous.nasaview.R;
import com.amiculous.nasaview.ui.AboutActivity;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class SettingsFragment extends Fragment {

    private Unbinder unbinder;
    private SettingsViewModel viewModel;
    @BindView(R.id.design_attribution_text) TextView designAttributionText;
    @BindView(R.id.apod_attribution_text) TextView apodAttributionText;
    @BindView(R.id.hd_switch) Switch wantHdSwitch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        unbinder = ButterKnife.bind(this, view);
        designAttributionText.setText(Html.fromHtml(getString(R.string.design_attribution)));
        designAttributionText.setMovementMethod(LinkMovementMethod.getInstance());

        apodAttributionText.setText(Html.fromHtml(getString(R.string.apod_attribution)));
        apodAttributionText.setMovementMethod(LinkMovementMethod.getInstance());

        boolean wantsHd = SharedPreferenceUtils.fetchWantsHD(view.getContext());
        wantHdSwitch.setChecked(wantsHd);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (BuildConfig.DEBUG) {
            AdView mAdView = Objects.requireNonNull(getActivity()).findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        MutableLiveData<Boolean> wantsHD= viewModel.getWantsHd();
        wantsHD.observe(getViewLifecycleOwner(), wantsHdNow -> SharedPreferenceUtils.storeWantsHD(getContext(),wantsHdNow));
    }

    @OnClick(R.id.about_button)
    void aboutButtonclicked() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.hd_switch)
    void onChecked(boolean isChecked) {
        viewModel.wantsHd.setValue(isChecked);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        viewModel.getWantsHd().removeObservers(this);
    }
}
