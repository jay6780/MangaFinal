package com.m.manga.Activity;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.m.manga.Fragment.HomePageFragment;
import com.m.manga.MyApplication;
import com.m.manga.Presenter.GenrePresenter;
import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.m.manga.View.GenreContract;
import com.m.manga.classes.ApiBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GenreContract.View {
    private ImageView btn_back5, moon,file_icon,App_logo,book_marked_icon,iv_font;
    private TextView book_marked_txt,tv_font;
    private RelativeLayout manyakol_title;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String FIRST_START = "FirstStart";
    private static final String NIGHT_MODE = "NightMode";
    private DrawerLayout drawerLayout;
    private LinearLayout navigationView,ll_darkmode,ll_file,ll_changeFont;
    private View viewSeperator;
    private FrameLayout fragment_container;
    private LinearLayout ll_defaultbg,ll_bookmarked;
    private GenreContract.Presenter genrePresenter;
    private TextView title,tv_appName,tv_ctto,tv_value_dark;
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
        tv_value_dark = findViewById(R.id.tv_value_dark);
        tv_appName = findViewById(R.id.tv_appName);
        tv_ctto = findViewById(R.id.tv_ctto);
        ll_defaultbg = findViewById(R.id.ll_defaultbg);
        drawerLayout = findViewById(R.id.drawer_layout);
        ll_darkmode = findViewById(R.id.ll_darkmode);
        navigationView = findViewById(R.id.nav_view);
        ll_file = findViewById(R.id.ll_file);
        file_icon = findViewById(R.id.file_icon);
        App_logo = findViewById(R.id.App_logo);
        viewSeperator = findViewById(R.id.viewSeperator);
        ll_bookmarked = findViewById(R.id.ll_bookmarked);
        book_marked_icon = findViewById(R.id.book_marked_icon);
        book_marked_txt = findViewById(R.id.book_marked_txt);
        ll_changeFont = findViewById(R.id.ll_changeFont);
        tv_font = findViewById(R.id.tv_font);
        iv_font = findViewById(R.id.iv_font);
        title = findViewById(R.id.title);

        title.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_appName.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_ctto.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_value_dark.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        book_marked_txt.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_font.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));

        float fontSize = SPUtils.getInstance().getFloat(Constants.fontSize,13f);

        if(fontSize == 13f){
            tv_font.setText("Small");
        }else if(fontSize == 15f){
            tv_font.setText("Medium");
        }else if(fontSize == 18f){
            tv_font.setText("Large");
        }

        ll_file.setOnClickListener(this);
        btn_back5.setOnClickListener(this);
        ll_darkmode.setOnClickListener(this);
        ll_bookmarked.setOnClickListener(this);
        ll_changeFont.setOnClickListener(this);

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
            tv_value_dark.setText("Dark mode");
            tv_ctto.setTextColor(Color.parseColor("#ffffff"));
            tv_appName.setTextColor(Color.parseColor("#ffffff"));
            tv_value_dark.setTextColor(Color.parseColor("#ffffff"));
            book_marked_txt.setTextColor(Color.parseColor("#ffffff"));
            tv_font.setTextColor(Color.parseColor("#ffffff"));
            title.setTextColor(Color.parseColor("#ffffff"));
            ll_defaultbg.setBackgroundColor(Color.parseColor("#262626"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.white));
            iv_font.setImageResource(R.mipmap.font_img);
            file_icon.setImageResource(R.mipmap.media_white);
            book_marked_icon.setImageResource(R.mipmap.bookmark_white);
            navigationView.setBackgroundColor(Color.parseColor("#262626"));
            manyakol_title.setBackgroundColor(Color.parseColor("#262626"));
        } else {
            btn_back5.setImageResource(R.mipmap.hamburger_black);
            moon.setImageResource(R.mipmap.moon_dark);
            iv_font.setImageResource(R.mipmap.font_imageblack);
            book_marked_txt.setTextColor(Color.parseColor("#000000"));
            tv_font.setTextColor(Color.parseColor("#000000"));
            title.setTextColor(Color.parseColor("#000000"));
            tv_value_dark.setText("Light mode");
            tv_ctto.setTextColor(Color.parseColor("#000000"));
            tv_appName.setTextColor(Color.parseColor("#000000"));
            tv_value_dark.setTextColor(Color.parseColor("#000000"));
            ll_defaultbg.setBackgroundColor(Color.parseColor("#EAEFEF"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.black));
            book_marked_icon.setImageResource(R.mipmap.bookmark_black);
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
            case R.id.ll_bookmarked:
                startActivity(new Intent(this, BookMarkedActivity.class));
                break;
            case R.id.ll_changeFont:
                String[] fontsize = {"Small", "Medium", "Large"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select font size");
                builder.setItems(fontsize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                SPUtils.getInstance().put(Constants.fontSize,13f);
                                recreate();
                                break;
                            case 1:
                                SPUtils.getInstance().put(Constants.fontSize,15f);
                                recreate();
                                break;
                            case 2:
                                SPUtils.getInstance().put(Constants.fontSize,18f);
                                recreate();
                                break;
                        }

                    }
                });
                builder.show();
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