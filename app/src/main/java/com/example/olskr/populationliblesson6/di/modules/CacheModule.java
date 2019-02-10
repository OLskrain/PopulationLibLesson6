package com.example.olskr.populationliblesson6.di.modules;

import com.example.olskr.populationliblesson6.mvp.model.cache.ICache;
import com.example.olskr.populationliblesson6.mvp.model.cache.RoomCache;

import dagger.Module;
import dagger.Provides;


@Module
public class CacheModule {

    @Provides
    public ICache cache(){
        return new RoomCache();
    }
//    @Provides
//    public ICache cache(){
//        return new Real();
//    }
}
