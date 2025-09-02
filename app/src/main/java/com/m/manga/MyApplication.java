package com.m.manga;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initGlide();
    }

    private void initGlide() {
        RequestOptions defaultOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false);

        Glide.init(this, new GlideBuilder()
                .setDefaultRequestOptions(defaultOptions));
    }
    public static void clearGlideMemoryCache() {
        Glide.get(instance).clearMemory();
    }

    public static void clearGlideDiskCache() {
        new Thread(() -> Glide.get(instance).clearDiskCache()).start();
    }

    public static void clearAllGlideCache() {
        clearGlideMemoryCache();
        clearGlideDiskCache();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}