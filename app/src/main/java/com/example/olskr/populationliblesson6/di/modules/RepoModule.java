package com.example.olskr.populationliblesson6.di.modules;

import com.example.olskr.populationliblesson6.mvp.model.api.IDataSource;
import com.example.olskr.populationliblesson6.mvp.model.cache.ICache;
import com.example.olskr.populationliblesson6.mvp.model.repo.UsersRepo;

import dagger.Module;
import dagger.Provides;


import javax.inject.Singleton;

@Module
public class RepoModule {

    @Singleton
    @Provides
    public UsersRepo usersRepo(ICache cache, IDataSource dataSource){
        return new UsersRepo(cache, dataSource);
    }

}
