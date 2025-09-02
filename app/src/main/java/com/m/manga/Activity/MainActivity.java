package com.m.manga.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.m.manga.Fragment.HomePageFragment;
import com.m.manga.MyApplication;
import com.m.manga.Presenter.GenrePresenter;
import com.m.manga.R;
import com.m.manga.View.GenreContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GenreContract.View {
    private ImageView btn_back5, moon,file_icon,App_logo;
    private TextView value_dark;
    private RelativeLayout manyakol_title;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String FIRST_START = "FirstStart";
    private static final String NIGHT_MODE = "NightMode";
    private DrawerLayout drawerLayout;
    private LinearLayout navigationView,ll_darkmode,ll_file;
    private View viewSeperator;
    private FrameLayout fragment_container;
    private LinearLayout ll_defaultbg;
    private GenreContract.Presenter genrePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appSettingsPrefs = getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isFirstStart = appSettingsPrefs.getBoolean(FIRST_START, true);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isFirstStart) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    isNightModeOn ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        }
        getSupportActionBar().hide();
        genrePresenter = new GenrePresenter(this);
        btn_back5 = findViewById(R.id.btn_back5);
        moon = findViewById(R.id.moon);
        fragment_container = findViewById(R.id.fragment_container);
        manyakol_title = findViewById(R.id.manyak_bg);
        value_dark = findViewById(R.id.value_dark);
        ll_defaultbg = findViewById(R.id.ll_defaultbg);
        drawerLayout = findViewById(R.id.drawer_layout);
        ll_darkmode = findViewById(R.id.ll_darkmode);
        navigationView = findViewById(R.id.nav_view);
        ll_file = findViewById(R.id.ll_file);
        file_icon = findViewById(R.id.file_icon);
        App_logo = findViewById(R.id.App_logo);
        viewSeperator = findViewById(R.id.viewSeperator);
        ll_file.setOnClickListener(this);
        btn_back5.setOnClickListener(this);
        ll_darkmode.setOnClickListener(this);
        MyApplication.clearAllGlideCache();
        Glide.with(MainActivity.this)
                .load(R.mipmap.app_logo)
                .circleCrop()
                .into(App_logo);

        updateUi(isDarkTheme());
        initStartGenre();

//        initPermission();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new HomePageFragment())
                    .commit();
        }

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                fragment_container.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                fragment_container.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                fragment_container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof HomePageFragment) {
            return ((HomePageFragment) fragment).handleKeyDown(keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void initGenreList() {
//        genre_spinner.setItems(genrelist);
//        genre_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//            @Override public void onItemSelected(MaterialSpinner view, int i, long id, String item) {
//                sharedViewModel.setSharedData(item);
//            }
//        });
//    }

    private void initStartGenre() {
        genrePresenter.getGenreList();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            } else {
                Log.e("granted","Manage All Files Access permission already granted");
            }
        }
    }

    private boolean isDarkTheme() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            btn_back5.setImageResource(R.mipmap.hamburger);
            moon.setImageResource(R.mipmap.moonlight);
            value_dark.setText("Dark mode");
            ll_defaultbg.setBackgroundColor(Color.parseColor("#262626"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.white));
            file_icon.setImageResource(R.mipmap.media_white);
            navigationView.setBackgroundColor(Color.parseColor("#262626"));
            manyakol_title.setBackgroundColor(Color.parseColor("#262626"));
        } else {
            btn_back5.setImageResource(R.mipmap.hamburger_black);
            moon.setImageResource(R.mipmap.moon_dark);
            value_dark.setText("Light mode");
            ll_defaultbg.setBackgroundColor(Color.parseColor("#EAEFEF"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.black));
            file_icon.setImageResource(R.mipmap.media_black);
            navigationView.setBackgroundColor(Color.parseColor("#EAEFEF"));
            manyakol_title.setBackgroundColor(Color.parseColor("#EAEFEF"));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragment_container.setVisibility(View.GONE);
            drawerLayout.requestDisallowInterceptTouchEvent(true);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back5:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    fragment_container.setVisibility(View.VISIBLE);
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    fragment_container.setVisibility(View.GONE);
                    drawerLayout.openDrawer(navigationView);
                }
                break;
            case R.id.ll_darkmode:
                boolean currentDarkMode = isDarkTheme();
                SharedPreferences.Editor editor = appSettingsPrefs.edit();
                drawerLayout.closeDrawer(navigationView);
                if (currentDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean(NIGHT_MODE, false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean(NIGHT_MODE, true);
                }

                editor.putBoolean(FIRST_START, false);
                editor.apply();
                updateUi(!currentDarkMode);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof ThemeChangeListener) {
                    ((ThemeChangeListener) currentFragment).onThemeChanged(!currentDarkMode);
                }
                break;
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideloading() {

    }

    @Override
    public void load_genre(ApiBean apiBean) {
//        if(apiBean.getGenre()!=null && !apiBean.getGenre().isEmpty()){
//            genrelist.addAll(apiBean.getGenre());
//            initGenreList();
////            Log.d("Genrelist","value: "+genrelist.toString());
//        }
    }

    public interface ThemeChangeListener {
        void onThemeChanged(boolean isDarkMode);
    }
}