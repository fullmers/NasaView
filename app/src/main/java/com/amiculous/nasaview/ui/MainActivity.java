package com.amiculous.nasaview.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.data.ApodEntity;
import com.amiculous.nasaview.ui.apod.ApodFragment;
import com.amiculous.nasaview.ui.favorite_details.FavoriteDetailsFragment;
import com.amiculous.nasaview.ui.favorites.FavoritesFragment;
import com.amiculous.nasaview.ui.settings.SettingsFragment;
import com.amiculous.nasaview.util.FirebaseUtils;
import com.amiculous.nasaview.util.SharedPreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        FavoritesFragment.FavoriteSelectListener {

    private static final String selectedFragmentKey = "SELECTED_FRAGMENT";
    private BottomNavigationView navigation;
    private int selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        int selected = R.id.navigation_apod;
        if (savedInstanceState != null) {
            Timber.i("savedInstanceState not Null, restoring state");
            selected = savedInstanceState.getInt(selectedFragmentKey,R.id.navigation_favorites);
        } else {
            Timber.i("savedInstanceState null, loading fresh");
        }
        navigation.setSelectedItemId(selected);
        showSelectedFragment(selected,false);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Take user back to Apod tab if not there, otherwise backpress normally
     */
    @Override
    public void onBackPressed() {
        //go back to favorites list fragment from details
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if(currentFrag instanceof FavoriteDetailsFragment) {
            navigation.setSelectedItemId(R.id.navigation_favorites);
            showSelectedFragment(R.id.navigation_favorites,false);
        } else { //otherwise, go back to main apod fragment from other fragments
            int selectedItemId = navigation.getSelectedItemId();
            if (R.id.navigation_apod != selectedItemId) {
                navigation.setSelectedItemId(R.id.navigation_apod);
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        showSelectedFragment(item.getItemId(),true);
        return true;
    }

    private void showSelectedFragment(int fragmentId, boolean keep) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        switch (fragmentId) {
            case R.id.navigation_favorites:
                commitFragment(FavoritesFragment.newInstance(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.FAVORITES_FRAGMENT);
                selectedFragment = R.id.navigation_favorites;
                break;
            case R.id.navigation_apod:
                commitFragment(ApodFragment.newInstance(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.APOD_FRAGMENT);
                selectedFragment = R.id.navigation_apod;
                break;
            case R.id.navigation_search:
                commitFragment(new SettingsFragment(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.SEARCH_FRAGMENT);
                selectedFragment = R.id.navigation_search;
                break;
            default:
                commitFragment(ApodFragment.newInstance(), keep);
                FirebaseUtils.screenShown(this, FirebaseUtils.APOD_FRAGMENT);
                selectedFragment = R.id.navigation_apod;
        }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(selectedFragmentKey, selectedFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFavoriteSelected(ApodEntity apod) {
        String apodString = SharedPreferenceUtils.apodToJSONstring(apod);
        Bundle bundle = new Bundle();
        bundle.putString(SharedPreferenceUtils.APOD_JSON_KEY,apodString);
        commitFragment(FavoriteDetailsFragment.newInstance(bundle), true);
      //  commitFragment(ApodFragment.newInstance(bundle), true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showSelectedFragment(R.id.navigation_favorites, true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
