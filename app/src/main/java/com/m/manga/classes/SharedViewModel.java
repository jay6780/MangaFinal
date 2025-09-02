package com.m.manga.classes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> genreData = new MutableLiveData<>();

    public LiveData<String> getGenreData() {
        return genreData;
    }

    public void setSharedData(String genre) {
        genreData.setValue(genre);
    }
}