package com.m.manga.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.m.manga.Adapter.GenreAdapter;
import com.m.manga.Adapter.PageAdapter;
import com.m.manga.Adapter.PageAdapter2;
import com.m.manga.Adapter.PageAdapter3;
import com.m.manga.Presenter.MangaPresenter;
import com.m.manga.R;
import com.m.manga.View.MangaContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.GenreData;
import com.m.manga.classes.Listerner.GenreNameListerner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageFragment extends Fragment implements MangaContract.View, View.OnClickListener, GenreNameListerner {
    private RecyclerView page_recycler,genre_recycler;
    private MangaContract.Presenter mangaPresenter;
    private ImageView expandIcon;
    private PageAdapter pageAdapter;
    private PageAdapter2 pageAdapter2;
    private PageAdapter3 pageAdapter3;
    private List<ApiBean.Data> datalist = new ArrayList<>();
    private List<ApiBean.Manga> mangalist = new ArrayList<>();
    private List<GenreData.Manga> genreList = new ArrayList<>();
    private boolean isLoading = false;
    private int page = 1;
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private EditText searchEditText;
    private ImageView btn_send;
    private String searchQuery = "";
    private boolean isPageAdapter2 = false;
    private SwipeRefreshLayout swipe;
    private boolean isRefresh = false;
    private boolean isError = false;
    private boolean isExpand = false;
    private GenreAdapter genreAdapter;
    private List<String> genrelist = new ArrayList<>();
    private String name = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(view);
        initRecycler();

        mangaPresenter = new MangaPresenter(this);
        mangaPresenter.getGenreList();

        appSettingsPrefs = getActivity().getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);
        if (isNightModeOn) {
            btn_send.setImageResource(R.mipmap.send_white);
            searchEditText.setTextColor(Color.parseColor("#ffffff"));
            searchEditText.setHintTextColor(Color.parseColor("#ffffff"));
        } else {
            btn_send.setImageResource(R.mipmap.send_img);
            searchEditText.setHintTextColor(Color.parseColor("#000000"));
            searchEditText.setTextColor(Color.parseColor("#000000"));
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });


        return view;

    }


    private void Refresh() {
        if(!isNetworkAvailable()){
            if (swipe.isRefreshing()) {
                swipe.setRefreshing(false);
            }
            Toast.makeText(getContext(),"Please check network and try again",Toast.LENGTH_SHORT).show();
            return;
        }
        isRefresh = true;
        page = 1;
        isLoading = false;
        isError = false;
        isPageAdapter2 = false;
        searchEditText.setText("");
        datalist.clear();
        mangalist.clear();
        isExpand = false;
        genreList.clear();
        expandIcon.setImageResource(R.mipmap.expand_more);
        mangaPresenter.getGenreList();
        genre_recycler.setVisibility(View.VISIBLE);
        expandIcon.setVisibility(View.VISIBLE);
        genreAdapter.expand(false);
        genre_recycler.scrollToPosition(0);
        initRecycler();
    }

    private void initRecycler() {
        page_recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pageAdapter = new PageAdapter(getContext(), datalist);
        page_recycler.setAdapter(pageAdapter);

        page_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isPageAdapter2 || isError) return;

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + 5)) {
                    page++;
                    loadGenre();
                }
            }
        });
    }



    private void loadGenre(){
        isLoading = true;
        mangaPresenter.loadGenre(name.toLowerCase().trim(),page);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void initView(View view) {
        expandIcon = view.findViewById(R.id.expandIcon);
        swipe = view.findViewById(R.id.swipe);
        searchEditText = view.findViewById(R.id.search);
        btn_send = view.findViewById(R.id.btn_send);
        page_recycler = view.findViewById(R.id.page_recycler);
        btn_send.setOnClickListener(this);
        genre_recycler = view.findViewById(R.id.genre_recycler);
        genre_recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        genreAdapter = new GenreAdapter(getContext(),genrelist,this);
        genre_recycler.setAdapter(genreAdapter);
        expandIcon.setOnClickListener(this);
        expandIcon.setImageResource(isExpand? R.mipmap.expand_less : R.mipmap.expand_more);
    }

    @Override
    public void showError(String message) {
        isError = true;
        Toast.makeText(getContext(),"Please check your internet or contact administrator",Toast.LENGTH_SHORT).show();
        isLoading = false;
    }

    @Override
    public void showLoading() {
        if(!isRefresh){
            swipe.setRefreshing(true);
        }
    }

    @Override
    public void hideloading() {
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }




    @Override
    public void loadDataSearch(ApiBean apiBean) {
        if (apiBean.getManga() != null && !apiBean.getManga().isEmpty()) {
            mangalist.clear();
            mangalist.addAll(apiBean.getManga());
            pageAdapter2.notifyDataSetChanged();
        }else{
            Toast.makeText(getContext(), "No anime in the list", Toast.LENGTH_SHORT).show();
        }
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    @Override
    public void loadGenreList(GenreData apiBean) {
        if (apiBean.getManga() != null && !apiBean.getManga().isEmpty()) {
            genreList.addAll(apiBean.getManga());
//            Log.d("getMangeGenre","value: "+apiBean.getManga());
            pageAdapter3.notifyDataSetChanged();
            isLoading = false;
        }else{
            Toast.makeText(getContext(), "No anime in the list", Toast.LENGTH_SHORT).show();
        }
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    @Override
    public void loadGenreName(ApiBean apiBean) {
        if (apiBean.getGenre() != null && !apiBean.getGenre().isEmpty()) {
            genrelist.clear();
            genrelist.addAll(apiBean.getGenre());

            Collections.sort(genrelist, (g1, g2) -> {
                if (g1.equalsIgnoreCase("All")) return -1;
                if (g2.equalsIgnoreCase("All")) return 1;
                return g1.compareToIgnoreCase(g2);
            });

            genreAdapter.firstHighlight(0);
            genreAdapter.notifyDataSetChanged();
        }

        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                searchQuery = searchEditText.getText().toString().trim();
                if (searchQuery.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter keyword", Toast.LENGTH_SHORT).show();
                    return;
                }

                isPageAdapter2 = true;
                isLoading = true;
                page = 1;
                datalist.clear();
                isError = false;
                mangalist.clear();
                genreList.clear();
                pageAdapter2 = new PageAdapter2(getContext(), mangalist);
                page_recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                page_recycler.setAdapter(pageAdapter2);
                mangaPresenter.loadSearch(searchQuery);
                genre_recycler.setVisibility(View.GONE);
                expandIcon.setVisibility(View.GONE);
                break;
            case R.id.expandIcon:
                isExpand = !isExpand;
                expandIcon.setImageResource(isExpand ? R.mipmap.expand_less : R.mipmap.expand_more);

                if(isExpand){
                    genre_recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    genreAdapter.expand(true);
                }else{
                    genre_recycler.setLayoutManager(
                            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
                    );
                    genreAdapter.expand(false);
                }
                break;
        }

    }

    @Override
    public void getName(String genreName) {
      if(genreName.isEmpty()){
          return;
      }

        name = genreName;
        page = 1;
        genreList.clear();
        datalist.clear();
        isPageAdapter2 = false;
        isRefresh = false;
        isError = false;
        mangaPresenter.loadGenre(genreName.toLowerCase().trim(),page);
        pageAdapter3 = new PageAdapter3(getContext(), genreList);
        page_recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        page_recycler.setAdapter(pageAdapter3);

    }

    @Override
    public void isExpand(boolean dismiss, int position) {
        if (dismiss) {
            genre_recycler.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            isExpand = false;
            expandIcon.setImageResource(R.mipmap.expand_more);
            genre_recycler.scrollToPosition(position);
        }

    }


    public boolean handleKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                scrollRecyclerViewBy(600);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                scrollRecyclerViewBy(-600);
                return true;
            default:
                return false;
        }
    }
    private void scrollRecyclerViewBy(int pixels) {
        page_recycler.smoothScrollBy(0, pixels);
    }



}