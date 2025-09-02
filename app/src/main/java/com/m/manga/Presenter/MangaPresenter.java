package com.m.manga.Presenter;

import com.m.manga.Model.MangaModel;
import com.m.manga.View.MangaContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.GenreData;
import com.m.manga.classes.Service.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MangaPresenter  implements MangaContract.Presenter {
    MangaContract.View mView;

    public MangaPresenter(MangaContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadGenre(String genre_name, int page) {
        mView.showLoading();
        MangaModel.getGenreList(genre_name,page, new Callback<GenreData>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(GenreData apiBean) {
                mView.hideloading();
                mView.loadGenreList(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
}


    @Override
    public void loadSearch(String keyword) {
        mView.showLoading();
        MangaModel.getSearchlist(keyword, new Callback<ApiBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(ApiBean apiBean) {
                mView.hideloading();
                mView.loadDataSearch(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
    }

    @Override
    public void getGenreList() {
        mView.showLoading();
        MangaModel.getGenre(new Callback<ApiBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(ApiBean apiBean) {
                mView.hideloading();
                mView.loadGenreName(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
    }

}