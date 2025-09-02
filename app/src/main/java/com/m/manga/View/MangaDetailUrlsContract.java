package com.m.manga.View;

import com.m.manga.classes.MangaImageBean;

public interface MangaDetailUrlsContract {
    interface View {
        void showError(String message);
        void showLoading();
        void hideloading();
        void loadImage(MangaImageBean apiBean);
    }

    interface Presenter {
        void getImages(String mangaId,String chapter);
    }
}
