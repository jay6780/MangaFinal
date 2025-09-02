package com.m.manga.Model;

import android.util.Log;

import com.m.manga.classes.ApiBean;
import com.m.manga.classes.Service.Callback;
import com.m.manga.classes.Service.NetworkingUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangaGenreModel {
    public static void getGenre(final Callback<ApiBean> callback) {
        NetworkingUtils.getTikTrendInstance()
                .getGenre()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApiBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(ApiBean data) {
                        callback.returnResult(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", "value: " + e.getMessage());
                        callback.returnError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {}
                });
    }
}
