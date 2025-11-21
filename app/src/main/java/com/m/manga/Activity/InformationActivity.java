package com.m.manga.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.m.manga.Adapter.InformationAdapter;
import com.m.manga.Fragment.ChapterFragment;
import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.m.manga.Utils.WindowUtils;
import com.m.manga.classes.AppConstant;
import com.m.manga.classes.OfflineData;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView thumbnail;
    private String thumbUrl;
    private String desc;
    private String animeTitle;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private RelativeLayout rl_bg;
    private ImageView btn_back5;
    private String Id;
    private ImageView refresh_btn;
    private CommonTabLayout dashboard_tab;
    private String[] mTitles;
    private ViewPager viewPager;
    private boolean isBookMark = false;
    private AppCompatButton btn_bookmark;
    private ArrayList<OfflineData> offlineDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().hide();
        thumbnail = findViewById(R.id.thumbnail);
        btn_bookmark = findViewById(R.id.btn_bookmark);
        btn_back5 = findViewById(R.id.btn_back5);
        dashboard_tab = findViewById(R.id.dashboard_tab);
        rl_bg = findViewById(R.id.rl_bg);
        refresh_btn = findViewById(R.id.refresh_btn);
        viewPager = findViewById(R.id.viewPager);
        thumbUrl = getIntent().getStringExtra("thumbUrl");
        desc = getIntent().getStringExtra("desc");
        animeTitle = getIntent().getStringExtra("animeTitle");
        Id = getIntent().getStringExtra("Id");

        dashboard_tab.setTextsize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        btn_bookmark.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        InformationAdapter adapter = new InformationAdapter(getSupportFragmentManager(), Id,desc);
        viewPager.setAdapter(adapter);
        refresh_btn.setOnClickListener(this);
        btn_back5.setOnClickListener(this);

        mTitles = new String[]{
                "Synopsis", "Chapters"

        };

        appSettingsPrefs = getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);

        SPUtils.getInstance().put(AppConstant.mangaId, Id);

        Glide.with(this)
                .load(thumbUrl)
                .into(thumbnail);

        updateUi(isNightModeOn);
        btn_back5.setImageResource(R.mipmap.back_black);
        refresh_btn.setBackgroundResource(R.drawable.circle_bg);
        refresh_btn.setImageResource(R.mipmap.refresh_black);
        btn_back5.setBackgroundResource(R.drawable.circle_bg);

        btn_bookmark.setOnClickListener(this);
        btn_bookmark.setTextColor(isBookMark ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        btn_bookmark.setText(isBookMark ? "Bookmarked" : "Not Bookmarked");
        btn_bookmark.setBackgroundResource(isBookMark ? R.drawable.bg_unselected : R.drawable.bg_selected);


        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("offlineBookmark", null);
        if (json != null) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<OfflineData>>() {}.getType();
            offlineDataArrayList = gson.fromJson(json, type);

            for (OfflineData data : offlineDataArrayList) {
                if (data.getId().equals(Id)) {
                    isBookMark = true;
                    break;
                }
            }
        } else {
            offlineDataArrayList = new ArrayList<>();
        }
        updateBookmarkUi();


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
                btn_bookmark.setVisibility(position == 0? View.VISIBLE:View.GONE);
                dashboard_tab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }
    private void updateBookmarkUi() {
        btn_bookmark.setTextColor(isBookMark ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        btn_bookmark.setText(isBookMark ? "Bookmarked" : "Not Bookmarked");
        btn_bookmark.setBackgroundResource(isBookMark ? R.drawable.bg_unselected : R.drawable.bg_selected);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new WindowUtils(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new WindowUtils(this);
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
            rl_bg.setBackgroundColor(Color.parseColor("#262626"));
        } else {
            dashboard_tab.setTextUnselectColor(getResources().getColor(R.color.black));
            rl_bg.setBackgroundColor(Color.parseColor("#EAEFEF"));

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
            case R.id.btn_bookmark:
                SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = sharedPreferences.getString("offlineBookmark", null);
                if (json != null) {
                    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<OfflineData>>() {
                    }.getType();
                    offlineDataArrayList = gson.fromJson(json, type);
                } else {
                    offlineDataArrayList = new ArrayList<>();
                }

                if (!isBookMark) {
                    boolean alreadyExists = false;
                    for (OfflineData data : offlineDataArrayList) {
                        if (data.getId().equals(Id)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        offlineDataArrayList.add(new OfflineData(System.currentTimeMillis(),desc, Id, animeTitle, thumbUrl));
                        Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < offlineDataArrayList.size(); i++) {
                        if (offlineDataArrayList.get(i).getId().equals(Id)) {
                            offlineDataArrayList.remove(i);
                            Toast.makeText(this, "Bookmark removed", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                String updatedJson = gson.toJson(offlineDataArrayList);
                editor.putString("offlineBookmark", updatedJson);
                editor.apply();

                isBookMark = !isBookMark;
                btn_bookmark.setTextColor(isBookMark ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                btn_bookmark.setText(isBookMark ? "Bookmarked" : "Not Bookmarked");
                btn_bookmark.setBackgroundResource(isBookMark ? R.drawable.bg_unselected : R.drawable.bg_selected);

                break;
        }
    }
}