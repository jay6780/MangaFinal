package com.m.manga.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.m.manga.Presenter.MangaDetailPresenter;
import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.m.manga.View.MangaDetailContract;
import com.m.manga.classes.ApiBean;

public class InformationFragment extends Fragment implements  MangaDetailContract.View {
    private TextView description, anime_title, genres;
    private static final String DESC = "desc";
    private static final String ID = "Id";
    private static final String Title = "title";
    private static final String THUMB = "thumbUrl";
    private String desc, title, thumbUrl;
    String id;
    private MangaDetailContract.Presenter detailPresenter;

    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";

    public static InformationFragment newInstance(String id, String desc, String title, String thumbUrl) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ID, id);
        args.putString(DESC, desc);
        args.putString(Title, title);
        args.putString(THUMB, thumbUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ID);
            desc = getArguments().getString(DESC);
            title = getArguments().getString(Title);
            thumbUrl = getArguments().getString(THUMB);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        description = view.findViewById(R.id.description);
        anime_title = view.findViewById(R.id.anime_title);
        genres = view.findViewById(R.id.genres);
        anime_title.setText("Title: " + title);
        if (desc == null) {
            description.setVisibility(View.GONE);
        }

        description.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        anime_title.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        genres.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        description.setText(desc);

        appSettingsPrefs = getActivity().getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
        updateUi(isNightModeOn);

        detailPresenter = new MangaDetailPresenter(this);
        detailPresenter.getDetail(id);

        return view;
    }

    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            genres.setTextColor(Color.parseColor("#ffffff"));
            description.setTextColor(Color.parseColor("#ffffff"));
            anime_title.setTextColor(Color.parseColor("#ffffff"));
        } else {
            genres.setTextColor(Color.parseColor("#000000"));
            description.setTextColor(Color.parseColor("#000000"));
            anime_title.setTextColor(Color.parseColor("#000000"));

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
    public void loadDetailData(ApiBean apiBean) {
        if (apiBean.getGenres() != null && !apiBean.getGenres().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String genrenames : apiBean.getGenres()) {
                sb.append(genrenames).append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            genres.setText("Genres: " + sb);

        } else {
            genres.setVisibility(View.GONE);
        }

    }
}