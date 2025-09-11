package com.m.manga.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.m.manga.Presenter.MangaDetailPresenter;
import com.m.manga.R;
import com.m.manga.View.MangaDetailContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.OfflineData;

import java.util.ArrayList;

public class InformationFragment extends Fragment implements  MangaDetailContract.View, View.OnClickListener {
    private TextView description, anime_title, genres;
    private AppCompatButton btn_bookmark;
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
    private boolean isBookMark = false;

    private ArrayList<OfflineData> offlineDataArrayList;

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
        btn_bookmark = view.findViewById(R.id.btn_bookmark);
        anime_title.setText("Title: " + title);
        if (desc == null) {
            description.setVisibility(View.GONE);
        }
        description.setText(desc);

        appSettingsPrefs = getActivity().getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);
        updateUi(isNightModeOn);

        detailPresenter = new MangaDetailPresenter(this);
        detailPresenter.getDetail(id);

        btn_bookmark.setOnClickListener(this);
        btn_bookmark.setTextColor(isBookMark ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        btn_bookmark.setText(isBookMark ? "Bookmarked" : "Not Bookmarked");
        btn_bookmark.setBackgroundResource(isBookMark ? R.drawable.bg_unselected : R.drawable.bg_selected);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("offlineBookmark", null);
        if (json != null) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<OfflineData>>() {}.getType();
            offlineDataArrayList = gson.fromJson(json, type);

            for (OfflineData data : offlineDataArrayList) {
                if (data.getId().equals(id)) {
                    isBookMark = true;
                    break;
                }
            }
        } else {
            offlineDataArrayList = new ArrayList<>();
        }
        updateBookmarkUi();
        
        return view;
    }

    private void updateBookmarkUi() {
        btn_bookmark.setTextColor(isBookMark ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
        btn_bookmark.setText(isBookMark ? "Bookmarked" : "Not Bookmarked");
        btn_bookmark.setBackgroundResource(isBookMark ? R.drawable.bg_unselected : R.drawable.bg_selected);
    }


    private void updateUi(boolean isDarkMode) {
        if (isDarkMode) {
            description.setTextColor(Color.parseColor("#ffffff"));
            anime_title.setTextColor(Color.parseColor("#ffffff"));
        } else {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bookmark:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
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
                        if (data.getId().equals(id)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        offlineDataArrayList.add(new OfflineData(desc, id, title, thumbUrl));
                        Toast.makeText(getContext(), "Bookmarked", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < offlineDataArrayList.size(); i++) {
                        if (offlineDataArrayList.get(i).getId().equals(id)) {
                            offlineDataArrayList.remove(i);
                            Toast.makeText(getContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
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