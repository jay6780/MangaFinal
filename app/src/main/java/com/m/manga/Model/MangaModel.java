package com.m.manga.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.m.manga.classes.ApiBean;
import com.m.manga.classes.GenreData;
import com.m.manga.classes.Service.Callback;
import com.m.manga.classes.Service.NetworkingUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class MangaModel {
    public static void getMangaList(int page, final Callback<ApiBean> callback) {
        NetworkingUtils.getTikTrendInstance()
                .getMangaData(page)
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


    public static void getSearchlist(String keyword, final Callback<ApiBean> callback) {
        NetworkingUtils.getTikTrendInstance()
                .getSearch(keyword)
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


    public static void getGenreList(String genre_name,int page, final Callback<GenreData> callback) {
        NetworkingUtils.getTikTrendInstance()
                .getGenreList(genre_name,page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<GenreData>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(GenreData data) {
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
