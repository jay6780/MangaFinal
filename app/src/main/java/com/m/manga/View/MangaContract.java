package com.m.manga.View;


import com.m.manga.classes.ApiBean;
import com.m.manga.classes.GenreData;

public interface MangaContract {
    interface View {
        void showError(String message);
        void showLoading();
        void hideloading();
        void loadDataSearch(ApiBean apiBean);
        void loadGenreList(GenreData apiBean);
        void loadGenreName(ApiBean apiBean);
    }

    interface Presenter {
        void loadGenre(String genre_name ,int page);
        void loadSearch(String keyword);
        void getGenreList();
    }

}