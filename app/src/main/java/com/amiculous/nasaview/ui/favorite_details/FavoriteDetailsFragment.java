package com.amiculous.nasaview.ui.favorite_details;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.ui.MainActivity;
import com.amiculous.nasaview.ui.favorites.FavoritesFragment;
import com.amiculous.nasaview.ui.favorites.FavoritesListAdapter;
import com.amiculous.nasaview.ui.favorites.FavoritesViewModel;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteDetailsFragment extends Fragment {

    @BindView(R.id.image) ImageView imageView;
  //  @BindView(R.id.toolbar) android.widget.Toolbar toolbar;
    private Unbinder unbinder;

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
        return inflater.inflate(R.layout.fragment_favorite_details, container, false);
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
            Picasso.get()
                    .load(apod.getUrl())
                    .placeholder(getResources().getDrawable(R.drawable.default_apod))
                    .into(imageView);

         //   ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
