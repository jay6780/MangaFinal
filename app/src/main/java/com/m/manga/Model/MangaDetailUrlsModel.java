package com.m.manga.Model;

import android.util.Log;

import com.m.manga.classes.MangaImageBean;
import com.m.manga.classes.Service.Callback;
import com.m.manga.classes.Service.NetworkingUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangaDetailUrlsModel {
    public static void getImages(String mangaId,String chapter, final Callback<MangaImageBean> callback) {
        NetworkingUtils.getTikTrendInstance()
                .getChapters(mangaId,chapter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MangaImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(MangaImageBean data) {
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
