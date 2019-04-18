package com.amiculous.nasaview.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.favorites)
    RecyclerView recyclerView;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    private FavoritesListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavoritesViewModel favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        adapter = new FavoritesListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        favoritesViewModel.getAllFavoriteApods().observe(getActivity(), apodEntities -> {
            Timber.i("Favorites:");
            for(ApodEntity apod: apodEntities) {
                Timber.i(apod.getTitle() + " " + apod.getId());
            }
            adapter.setFavorites(apodEntities);
        });
    }

}
