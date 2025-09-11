package com.m.manga.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m.manga.Adapter.OfflineAdapter;
import com.m.manga.Adapter.PageAdapter;
import com.m.manga.R;
import com.m.manga.classes.OfflineData;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BookMarkedActivity extends AppCompatActivity {
    private LinearLayout ll_bg,ll_empty;
    private ImageView btn_back,empty_icon;
    private TextView title,book_mark_emptyTxt;
    private RecyclerView bookmarkRecycler;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private View viewSeperator;
    private ArrayList<OfflineData> offlineDataArrayList;
    private OfflineAdapter offlineAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marked);
        getSupportActionBar().hide();
        viewSeperator = findViewById(R.id.viewSeperator);
        ll_bg = findViewById(R.id.ll_bg);
        btn_back = findViewById(R.id.btn_back);
        ll_empty = findViewById(R.id.ll_empty);
        empty_icon = findViewById(R.id.empty_icon);
        title = findViewById(R.id.title);
        book_mark_emptyTxt = findViewById(R.id.book_mark_emptyTxt);
        btn_back.setOnClickListener(v -> finish());
        bookmarkRecycler = findViewById(R.id.bookmarkRecycler);
        appSettingsPrefs = getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);

        if(isNightModeOn){
            ll_bg.setBackgroundColor(Color.parseColor("#262626"));
            title.setTextColor(Color.parseColor("#ffffff"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.white));
            btn_back.setImageResource(R.mipmap.back_white);
            empty_icon.setImageResource(R.mipmap.trash_white);
            book_mark_emptyTxt.setTextColor(Color.parseColor("#ffffff"));

        }else{
            ll_bg.setBackgroundColor(Color.parseColor("#EAEFEF"));
            title.setTextColor(Color.parseColor("#000000"));
            viewSeperator.setBackgroundColor(getResources().getColor(R.color.black));
            btn_back.setImageResource(R.mipmap.back_black);
            empty_icon.setImageResource(R.mipmap.trash_black);
            book_mark_emptyTxt.setTextColor(Color.parseColor("#000000"));
        }
        loadData();
        bookmarkRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        offlineAdapter = new OfflineAdapter(this, offlineDataArrayList);
        bookmarkRecycler.setAdapter(offlineAdapter);



    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("offlineBookmark", null);
        Type type = new TypeToken<ArrayList<OfflineData>>() {}.getType();
        offlineDataArrayList = gson.fromJson(json, type);
        if (offlineDataArrayList == null) {
            offlineDataArrayList = new ArrayList<>();
        }
        updateUi();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        updateUi();
        offlineAdapter.updateData(offlineDataArrayList);
    }

    private void updateUi(){
        if(offlineDataArrayList.isEmpty()){
            ll_empty.setVisibility(View.VISIBLE);
            bookmarkRecycler.setVisibility(View.GONE);
        }else{
            ll_empty.setVisibility(View.GONE);
            bookmarkRecycler.setVisibility(View.VISIBLE);
        }

    }


}