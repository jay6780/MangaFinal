package com.m.manga.classes.Service;

public class NetworkingUtils {

    private static ApiService apiService;

    public static ApiService getTikTrendInstance() {
        if (apiService == null)
            apiService = RetrofitAdapter.getInstance().create(ApiService.class);

        return apiService;
    }

}