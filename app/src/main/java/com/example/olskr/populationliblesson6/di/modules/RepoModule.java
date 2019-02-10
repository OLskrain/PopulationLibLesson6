package com.example.olskr.populationliblesson6.di.modules;

import com.example.olskr.populationliblesson6.mvp.model.api.IDataSource;
import com.example.olskr.populationliblesson6.mvp.model.cache.ICache;
import com.example.olskr.populationliblesson6.mvp.model.repo.UsersRepo;

import dagger.Module;
import dagger.Provides;


import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class RepoModule {

    @Singleton //Dagger будет хранить только один
    @Provides
    //здесь нам нужен ICache. и внутри Dagger мы его можем получить без @Inject, так как мы добавили в
    //AppComponent соответствующий модуль CacheModule. В других местах (вне Dagger) нам нажно прописывать аннотацию
    public UsersRepo usersRepo(ICache cache, IDataSource dataSource){
        return new UsersRepo(cache, dataSource);
    }

}
