package com.amiculous.nasaview.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.api.NetworkUtils;
import com.amiculous.nasaview.ui.apod.ApodFragment;
import com.amiculous.nasaview.ui.favorites.FavoritesFragment;
import com.amiculous.nasaview.ui.search.SearchFragment;
import com.amiculous.nasaview.util.FirebaseUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView navigation;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtils.buildUrl();
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_apod);
        commitFragment(ApodFragment.newInstance(), false);
        navigation.setOnNavigationItemSelectedListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /**
     * Take user back to Apod tab if not there, otherwise backpress normally
     */
    @Override
    public void onBackPressed() {
        int selectedItemId = navigation.getSelectedItemId();
        if (R.id.navigation_apod != selectedItemId) {
            navigation.setSelectedItemId(R.id.navigation_apod);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        boolean keep = true;
        switch (item.getItemId()) {
            case R.id.navigation_favorites:
                commitFragment(FavoritesFragment.newInstance(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.FAVORITES_FRAGMENT);
                break;
            case R.id.navigation_apod:
                commitFragment(ApodFragment.newInstance(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.APOD_FRAGMENT);
                break;
            case R.id.navigation_search:
                commitFragment(new SearchFragment(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.SEARCH_FRAGMENT);
                break;
        }
        return true;
    }

    private void commitFragment(@NonNull Fragment fragment, boolean keep) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (keep) {
            Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (oldFragment != null) {
                transaction.hide(oldFragment);
            }
            transaction.add(R.id.frame_container, fragment);
            transaction.addToBackStack(fragment.getClass().getName());
        } else {
            transaction.replace(R.id.frame_container, fragment);
        }
        transaction.commit();
    }

    //TODO make an about/settings fragment and display this :
    //Saturn icon and Telescope icon::
    //<div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/"title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

}
