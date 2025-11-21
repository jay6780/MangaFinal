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

import java.util.stream.Collectors;

public class InformationFragment extends Fragment implements  MangaDetailContract.View {
    private TextView description, anime_title,tv_genres,status;
    private static final String ID = "Id";
    private static final String DESC = "desc";
    String id,desc;
    private MangaDetailContract.Presenter detailPresenter;

    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";

    public static InformationFragment newInstance(String id,String desc) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ID, id);
        args.putString(DESC, desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ID);
            desc = getArguments().getString(DESC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        description = view.findViewById(R.id.description);
        anime_title = view.findViewById(R.id.anime_title);
        tv_genres = view.findViewById(R.id.genres);
        status = view.findViewById(R.id.status);
        description.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        anime_title.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        status.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_genres.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        appSettingsPrefs = getActivity().getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
        updateUi(isNightModeOn);
        detailPresenter = new MangaDetailPresenter(this);
        detailPresenter.getDetail(id);
        description.setVisibility(desc == null? View.GONE:View.VISIBLE);
        description.setText("Description: "+desc);
        return view;
    }

    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            tv_genres.setTextColor(Color.parseColor("#ffffff"));
            description.setTextColor(Color.parseColor("#ffffff"));
            anime_title.setTextColor(Color.parseColor("#ffffff"));
            status.setTextColor(Color.parseColor("#ffffff"));
        } else {
            tv_genres.setTextColor(Color.parseColor("#000000"));
            description.setTextColor(Color.parseColor("#000000"));
            anime_title.setTextColor(Color.parseColor("#000000"));
            status.setTextColor(Color.parseColor("#000000"));
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
            StringBuilder str = new StringBuilder();
            for(String genres : apiBean.getGenres()){
                str.append(genres).append(", ");
            }
            if (str.length() > 0) {
                str.setLength(str.length() - 2);
            }
            anime_title.setVisibility(apiBean.getTitle() == null && apiBean.getTitle().isEmpty()? View.GONE :View.VISIBLE);
            status.setVisibility(apiBean.getTitle() == null && apiBean.getTitle().isEmpty()? View.GONE:View.VISIBLE);
            anime_title.setText("Title: " + apiBean.getTitle());
            status.setText("Status : "+apiBean.getStatus());
            tv_genres.setText("Genres: " + str);
            tv_genres.setVisibility(View.VISIBLE);
        } else {
            tv_genres.setVisibility(View.GONE);
        }
    }
}