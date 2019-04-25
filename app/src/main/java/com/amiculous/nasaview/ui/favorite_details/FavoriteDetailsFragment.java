package com.amiculous.nasaview.ui.favorite_details;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.databinding.FragmentFavoriteDetailsBinding;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class FavoriteDetailsFragment extends Fragment {

    @BindView(R.id.image) ImageView imageView;
    private Unbinder unbinder;
    private FavoriteApodViewModel viewmodel;

    public FavoriteDetailsFragment() {}

    public static FavoriteDetailsFragment newInstance(Bundle favoritesBundle) {
        FavoriteDetailsFragment fragment = new FavoriteDetailsFragment();
        fragment.setArguments(favoritesBundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewmodel = ViewModelProviders.of(this).get(FavoriteApodViewModel.class);
        FragmentFavoriteDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_details, container, false);
        binding.setViewmodel(viewmodel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        unbinder = ButterKnife.bind(this, view);

        Bundle arguments = bundle == null ? getArguments() : bundle;

        if (arguments == null) {
            throw new IllegalArgumentException("Missing arguments in FavoriteDetailsFragment");
        } else {
            String apodJson = arguments.getString(SharedPreferenceUtils.APOD_JSON_KEY);
            Timber.i(apodJson);
            ApodEntity apod = SharedPreferenceUtils.jsonToApod(apodJson);
            viewmodel.init(apod);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
