package com.m.manga.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.m.manga.Adapter.DetailAdapter;
import com.m.manga.Presenter.MangaDetailPresenter;
import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.m.manga.View.MangaDetailContract;
import com.m.manga.classes.ApiBean;

import java.util.ArrayList;
import java.util.List;

public class ChapterFragment extends Fragment implements View.OnClickListener, MangaDetailContract.View {
    private TextView ascending, descending;
    private ListView listView;
    private DetailAdapter detailAdapter;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private List<ApiBean.Chapters> chaptersList = new ArrayList<>();
    private List<ApiBean.Chapters> DescList = new ArrayList<>();
    private List<ApiBean.Chapters> AscList = new ArrayList<>();
    MangaDetailContract.Presenter detailPresenter;
    private KProgressHUD kProgressHUD;
    private static final String ARG_ID = "Id";
    private String Id;

    public static ChapterFragment newInstance(String Id) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chapter_fragment, container, false);
        appSettingsPrefs = getActivity().getSharedPreferences(PREF, MODE_PRIVATE);
        ascending = view.findViewById(R.id.ascending);
        descending = view.findViewById(R.id.descending);
        listView = view.findViewById(R.id.ListViewData);
        ascending.setOnClickListener(this);
        descending.setOnClickListener(this);
        detailAdapter = new DetailAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(detailAdapter);
        detailPresenter = new MangaDetailPresenter(this);
        detailPresenter.getDetail(Id);
        ascending.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        descending.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.descending:
                isSort(true);
                break;
            case R.id.ascending:
                isSort(false);
                break;
        }
    }
    private void isSort(boolean desc) {
        int h1 = listView.getHeight();
        listView.smoothScrollToPositionFromTop(0, h1/2,500);
        if (desc) {
            boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
            if (isNightModeOn) {
                ascending.setTextColor(Color.parseColor("#ffffff"));
                descending.setTextColor(Color.parseColor("#000000"));
                descending.setBackgroundResource(R.drawable.bg_dark);
            } else {
                descending.setTextColor(Color.parseColor("#ffffff"));
                descending.setBackgroundResource(R.drawable.bg_light);
                ascending.setTextColor(Color.parseColor("#000000"));
            }
            ascending.setBackgroundResource(0);
            detailAdapter.descData(DescList);
        } else {
            boolean isNightModeOn2 = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
            if (isNightModeOn2) {
                descending.setTextColor(Color.parseColor("#ffffff"));
                ascending.setTextColor(Color.parseColor("#000000"));
                ascending.setBackgroundResource(R.drawable.bg_dark);
            } else {
                descending.setTextColor(Color.parseColor("#000000"));
                ascending.setTextColor(Color.parseColor("#ffffff"));
                ascending.setBackgroundResource(R.drawable.bg_light);
            }
            descending.setBackgroundResource(0);
            detailAdapter.ascData(AscList);
        }

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), "Please check your internet or contact administrator", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait");
        getActivity().runOnUiThread(() -> kProgressHUD.show());
    }

    @Override
    public void hideloading() {
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            getActivity().runOnUiThread(() -> kProgressHUD.dismiss());
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadDetailData(ApiBean apiBean) {
        if (apiBean != null && apiBean.getChapters() != null) {
            chaptersList.clear();
            chaptersList.addAll(apiBean.getChapters());
            DescList.addAll(apiBean.getChapters());
            AscList.addAll(apiBean.getChapters());
            isSort(false);
            detailAdapter.ascData(chaptersList);
        } else {
            listView.setVisibility(View.GONE);
            detailAdapter.ascData(new ArrayList<>());
            Toast.makeText(getContext(), "Empty information", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshData() {
        if (Id != null) {
            detailAdapter.descData(new ArrayList<>());
            detailAdapter.ascData(new ArrayList<>());
            detailPresenter.getDetail(Id);
        }
    }
}