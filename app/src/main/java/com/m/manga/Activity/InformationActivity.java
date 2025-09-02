package com.m.manga.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.m.manga.Adapter.InformationAdapter;
import com.m.manga.Fragment.ChapterFragment;
import com.m.manga.Fragment.InformationFragment;
import com.m.manga.R;
import com.m.manga.classes.AppConstant;
import com.m.manga.classes.SPUtils;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView thumbnail;
    private String thumbUrl;
    private String desc;
    private String animeTitle;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private LinearLayout linear_b2;
    private boolean isExpanded = false;
    private ImageView btn_back5;
    private String Id;
    private ImageView refresh_btn;
    private CommonTabLayout dashboard_tab;
    private String[] mTitles;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        thumbnail = findViewById(R.id.thumbnail);

        btn_back5 = findViewById(R.id.btn_back5);
        dashboard_tab = findViewById(R.id.dashboard_tab);
        linear_b2 = findViewById(R.id.linear_b2);
        refresh_btn = findViewById(R.id.refresh_btn);
        viewPager = findViewById(R.id.viewPager);
        thumbUrl = getIntent().getStringExtra("thumbUrl");
        desc = getIntent().getStringExtra("desc");
        animeTitle = getIntent().getStringExtra("animeTitle");
        Id = getIntent().getStringExtra("Id");

        InformationAdapter adapter = new InformationAdapter(getSupportFragmentManager(), Id,animeTitle,desc);
        viewPager.setAdapter(adapter);
        refresh_btn.setOnClickListener(this);
        btn_back5.setOnClickListener(this);

        mTitles = new String[]{
                "Synopsis", "Chapters"

        };

        appSettingsPrefs = getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);

        SPUtils.getInstance().put(AppConstant.mangaId, Id);

        Glide.with(this)
                .load(thumbUrl)
                .centerCrop()
                .into(thumbnail);

        updateUi(isNightModeOn);
        btn_back5.setImageResource(R.mipmap.back_black);
        refresh_btn.setBackgroundResource(R.drawable.circle_bg);
        refresh_btn.setImageResource(R.mipmap.refresh_black);
        btn_back5.setBackgroundResource(R.drawable.circle_bg);


        ArrayList<CustomTabEntity> list = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            final int j = i;
            list.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return mTitles[j];
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            });
        }

        dashboard_tab.setTabData(list);

        dashboard_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                refresh_btn.setVisibility(position == 0? View.GONE:View.VISIBLE);
                dashboard_tab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void Refresh() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Please check network and try again", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ChapterFragment) {
                ((ChapterFragment) fragment).refreshData();
            }
        }
    }
    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            dashboard_tab.setTextUnselectColor(getResources().getColor(R.color.white));
            btn_back5.setBackgroundResource(R.drawable.circle_bg);
            linear_b2.setBackgroundColor(Color.parseColor("#262626"));
        } else {
            dashboard_tab.setTextUnselectColor(getResources().getColor(R.color.black));
            linear_b2.setBackgroundColor(Color.parseColor("#EAEFEF"));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back5:
                finish();
                break;
            case R.id.refresh_btn:
                Refresh();
                break;
        }
    }
}