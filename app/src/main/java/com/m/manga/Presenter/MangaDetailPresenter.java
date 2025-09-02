package com.m.manga.Presenter;

import com.m.manga.Model.MangaDetailModel;
import com.m.manga.View.MangaContract;
import com.m.manga.View.MangaDetailContract;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.Service.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MangaDetailPresenter implements MangaDetailContract.Presenter {
    MangaDetailContract.View mView;
    public MangaDetailPresenter(MangaDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getDetail(String mangaId) {
        mView.showLoading();
        MangaDetailModel.getDetailData(mangaId, new Callback<ApiBean>() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void returnResult(ApiBean apiBean) {
                mView.hideloading();
                mView.loadDetailData(apiBean);
            }

            @Override
            public void returnError(String message) {
                mView.hideloading();
                mView.showError(message);
            }
        });
    }
}
