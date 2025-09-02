package com.m.manga.View;

import com.m.manga.classes.ApiBean;

public interface GenreContract {
    interface View {
        void showError(String message);
        void showLoading();
        void hideloading();
        void load_genre(ApiBean apiBean);
    }

    interface Presenter {
        void getGenreList();
    }

}
