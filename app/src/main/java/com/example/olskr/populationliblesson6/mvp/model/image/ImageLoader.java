package com.example.olskr.populationliblesson6.mvp.model.image;

import android.support.annotation.Nullable;

public interface ImageLoader<T> { //абстракция для загружаемых картинок.от куда куда
    void loadInto(@Nullable String url, T container);
}
