package com.m.manga.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.m.manga.Adapter.ImageAdapter;
import com.m.manga.Presenter.MangaImagePresenter;
import com.m.manga.R;
import com.m.manga.View.MangaDetailUrlsContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.MangaImageBean;

import java.util.ArrayList;
import java.util.List;

public class ViewChapterActivity extends AppCompatActivity implements MangaDetailUrlsContract.View {
    private RecyclerView chapterImageRecycler;
    private MangaDetailUrlsContract.Presenter imageUrlPresenter;
    private String chapterId;
    private String Id;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private KProgressHUD kProgressHUD;
    private ImageView btn_back5;
    private LinearLayout linear_bg;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private View viewSeperator;
    private ImageView delete_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chapter);
        getSupportActionBar().hide();
        Id = getIntent().getStringExtra("Id");
        chapterId = getIntent().getStringExtra("chapterId");
        btn_back5 = findViewById(R.id.btn_back5);
        linear_bg = findViewById(R.id.linear_bg);
        delete_btn = findViewById(R.id.delete_btn);
        chapterImageRecycler = findViewById(R.id.chapterImageRecycler);
        chapterImageRecycler.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new ImageAdapter(this,imageUrls);
        chapterImageRecycler.setAdapter(imageAdapter);
        imageUrlPresenter = new MangaImagePresenter(this);
        imageUrlPresenter.getImages(Id,chapterId);
        delete_btn.setVisibility(View.VISIBLE);
        viewSeperator = findViewById(R.id.viewSeperator);
        appSettingsPrefs = getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
        updateUi(isNightModeOn);
        delete_btn.setOnClickListener(v -> Refresh());
        btn_back5.setOnClickListener(v -> finish());
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void Refresh() {
        if(!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Please check network and try again",Toast.LENGTH_SHORT).show();
            return;
        }
        imageUrls.clear();
        imageAdapter.notifyDataSetChanged();
        imageUrlPresenter.getImages(Id,chapterId);
    }

    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            delete_btn.setImageResource(R.mipmap.refresh_white);
            btn_back5.setImageResource(R.mipmap.back_white);
            viewSeperator.setBackgroundColor(Color.parseColor("#ffffff"));
            linear_bg.setBackgroundColor(Color.parseColor("#262626"));
        } else {
            delete_btn.setImageResource(R.mipmap.refresh_black);
            btn_back5.setImageResource(R.mipmap.back_black);
            viewSeperator.setBackgroundColor(Color.parseColor("#000000"));
            linear_bg.setBackgroundColor(Color.parseColor("#EAEFEF"));

        }
    }
    @Override

    public void showError(String message) {
//        Log.e("ErrorData","value: "+message);
        Toast.makeText(getApplicationContext(),"Please check your internet or contact administrator",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showLoading() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait");
        kProgressHUD.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                scrollRecyclerViewBy(600);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                scrollRecyclerViewBy(-600);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }


    private void scrollRecyclerViewBy(int pixels) {
        chapterImageRecycler.smoothScrollBy(0, pixels);
    }

    @Override
    public void hideloading() {
        if (!isFinishing() && kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void loadImage(MangaImageBean apiBean) {
      if(apiBean.getImageUrls()!=null && !apiBean.getImageUrls().isEmpty()){
          imageUrls.addAll(apiBean.getImageUrls());
          imageAdapter.notifyDataSetChanged();
      }else{
          Toast.makeText(getApplicationContext(), "Empty image list", Toast.LENGTH_SHORT).show();
      }
    }
}