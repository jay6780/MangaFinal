package com.m.manga.View;

import com.m.manga.classes.ApiBean;

public interface MangaDetailContract {
    interface View {
        void showError(String message);
        void showLoading();
        void hideloading();
        void loadDetailData(ApiBean apiBean);
    }

    interface Presenter {
        void getDetail(String mangaId);
    }

}
