package com.example.olskr.populationliblesson6;

import android.app.Application;

import com.example.olskr.populationliblesson6.di.AppComponent;
import com.example.olskr.populationliblesson6.di.DaggerAppComponent;
import com.example.olskr.populationliblesson6.di.modules.AppModule;
import com.example.olskr.populationliblesson6.mvp.model.entity.room.db.UserDatabase;


import io.realm.Realm;
import timber.log.Timber;

public class App extends Application {
    private static App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Timber - Logger, который работает без тегов, он сам знает от куда лог пришел
        Timber.plant(new Timber.DebugTree());
        Realm.init(this);
        UserDatabase.create(this); //вызываем создание базы Room

        //подключение Dagger
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
