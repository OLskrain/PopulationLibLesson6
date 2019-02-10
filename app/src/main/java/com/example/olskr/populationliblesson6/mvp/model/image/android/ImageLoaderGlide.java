package com.example.olskr.populationliblesson6.mvp.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.olskr.populationliblesson6.NetworkStatus;
import com.example.olskr.populationliblesson6.mvp.model.cache.ImageCache;
import com.example.olskr.populationliblesson6.mvp.model.image.ImageLoader;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import timber.log.Timber;

public class ImageLoaderGlide implements ImageLoader<ImageView> {

    @Override
    //здесь не обязательно самим прописывать действия и кешироавания. можно использовать кеш glide
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOnline()) {
            //благодаря модулю можем GlideModule, можем использовать расширенные возможности Glide
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Image load failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    //сюда приехал наш bitmap
                    ImageCache.saveImage(url, resource);//сохранили
                    return false;
                }
            }).into(container);
        } else {
            //если не онлайн
            if (ImageCache.contains(url)) {
                GlideApp.with(container.getContext())
                        .load(ImageCache.getFile(url))
                        .into(container);
            }
        }
    }

}
