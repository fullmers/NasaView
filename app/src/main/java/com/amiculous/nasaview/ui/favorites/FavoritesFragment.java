package com.amiculous.nasaview.ui.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.util.SharedPreferenceUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class FavoritesFragment extends Fragment implements FavoritesListAdapter.FavoriteSelectionListener {

    @BindView(R.id.favorites) RecyclerView recyclerView;
    @BindView(R.id.no_favorites_layout) LinearLayout noFavoritesLayout;

    private Unbinder unbinder;
    private FavoritesViewModel favoritesViewModel;
    private FavoritesListAdapter adapter;
    private ImageView detailsImageView;

    private FavoriteSelectListener favoriteSelectListener;

    public interface FavoriteSelectListener {
        void onFavoriteSelected(ApodEntity apod);
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        adapter = new FavoritesListAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        favoritesViewModel.getAllFavoriteApods().observe(this, apodEntities -> {
            if (apodEntities.size() == 0) {
                Timber.i("No Favorites yet");
                recyclerView.setVisibility(View.GONE);
                noFavoritesLayout.setVisibility(View.VISIBLE);
            } else {
                Timber.i("Favorites:");
                for (ApodEntity apod : apodEntities) {
                    Timber.i(apod.getTitle() + " " + apod.getId());
                }
                recyclerView.setVisibility(View.VISIBLE);
                noFavoritesLayout.setVisibility(View.GONE);
                adapter.setFavorites(apodEntities);
            }
        });

        detailsImageView = getActivity().findViewById(R.id.preview_image);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            favoriteSelectListener = (FavoriteSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FavoriteSelectListener");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        favoritesViewModel.getAllFavoriteApods().removeObservers(this);
    }

    @Override
    public void onFavoriteSelected(ApodEntity apod, ImageView preview) {
        Timber.i("tapped: " + apod.getTitle());
        Bundle bundle = new Bundle();
        bundle.putString(SharedPreferenceUtils.APOD_JSON_KEY,SharedPreferenceUtils.apodToJSONstring(apod));
        favoriteSelectListener.onFavoriteSelected(apod);
    }
}
