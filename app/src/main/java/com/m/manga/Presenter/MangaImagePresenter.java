package com.m.manga.Presenter;

import com.m.manga.Model.MangaDetailUrlsModel;
import com.m.manga.View.MangaDetailUrlsContract;
import com.m.manga.classes.MangaImageBean;
import com.m.manga.classes.Service.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MangaImagePresenter  implements MangaDetailUrlsContract.Presenter {
    MangaDetailUrlsContract.View mView;
    public MangaImagePresenter(MangaDetailUrlsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getImages(String mangaId,String chapter) {
        mView.showLoading();
        MangaDetailUrlsModel.getImages(mangaId,chapter, new Callback<MangaImageBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(MangaImageBean apiBean) {
                mView.hideloading();
                mView.loadImage(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
    }
}
