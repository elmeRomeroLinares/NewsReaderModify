package com.example.newsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.newsreader.fragment.GeneralPagerFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // View pager
    @BindView (R.id.pager)
    ViewPager mNewsCategoriesVP;

    // Tab Layout for View Pager
    @BindView(R.id.tab_layout)
    TabLayout mTabNewsCategories;

    public static final String DAYNIGHT = "DAY_NIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //TODO new listener to know when tab was selected and perform read later refresh
        mNewsCategoriesVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 5) {
                    Fragment page = getSupportFragmentManager().
                            findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
                    if (page != null) {
                        ((GeneralPagerFragment) page).reload();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Setup Pager with Tab
        mNewsCategoriesVP.setAdapter(new CategoriesPagerAdapter(getSupportFragmentManager()));
        mTabNewsCategories.setupWithViewPager(mNewsCategoriesVP);
    }

    @OnClick(R.id.floatingActionButton)
    public void searchPanel() {
        Intent searchIntent = new Intent(MainActivity.this, PanelSearchView.class);
        startActivity(searchIntent);
    }

    //TODO decide which mode is active and perform setDefaultNightMode()
    @OnClick(R.id.day_night_action_button)
    public void performDayNight() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(DAYNIGHT, true);
                break;
            // Night mode is not active, we're in day time
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(DAYNIGHT, false);
                break;
            // Night mode is active, we're at night!
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                break;
        }
        editor.apply();
        recreate();
    }
}