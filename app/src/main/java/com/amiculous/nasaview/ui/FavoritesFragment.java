package com.amiculous.nasaview.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amiculous.nasaview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    @BindView(R.id.favorites) RecyclerView recyclerView;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        ButterKnife.bind(this, rootView);

        final FavoritesListAdapter adapter = new FavoritesListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        // TODO: Use the ViewModel
    }

}
