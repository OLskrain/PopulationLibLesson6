package com.example.olskr.populationliblesson6.di.modules;

import com.example.olskr.populationliblesson6.App;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule { //подключаем App к Dagger

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    public App app(){
       return app;
    }
}
