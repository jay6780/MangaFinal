package com.m.manga.Presenter;

import com.m.manga.Model.MangaGenreModel;
import com.m.manga.View.GenreContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.Service.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class GenrePresenter implements GenreContract.Presenter {
    GenreContract.View mView;

    public GenrePresenter(GenreContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getGenreList() {
        mView.showLoading();
        MangaGenreModel.getGenre(new Callback<ApiBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(ApiBean apiBean) {
                mView.hideloading();
                mView.load_genre(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
    }
}