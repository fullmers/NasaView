package com.amiculous.nasaview.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.amiculous.nasaview.R;
import com.amiculous.nasaview.ui.apod.ApodFragment;
import com.amiculous.nasaview.ui.favorites.FavoritesFragment;
import com.amiculous.nasaview.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.design.widget.BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.design.widget.CoordinatorLayout.LayoutParams layoutParams = (android.support.design.widget.CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBarBehavior());

        navigation.setSelectedItemId(R.id.navigation_apod);
        loadFragment(ApodFragment.newInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment favorites = FavoritesFragment.newInstance();
            switch (item.getItemId()) {
                case R.id.navigation_favorites:
                    loadFragment(favorites);
                    return true;
                case R.id.navigation_apod:
                    Fragment apod = ApodFragment.newInstance();
                    loadFragment(apod);
                    return true;
                case R.id.navigation_search:
                    Fragment search = new SearchFragment();
                    loadFragment(search);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        //TODO this back navigation is funny. figure out why and fix it.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //TODO make an about/settings fragment and display this :
    //Saturn icon:
    //<div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/"title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
    //Telescope icon:
    //<div>Icons made by <a href="https://www.freepik.com/" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/"title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/"title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>


}
