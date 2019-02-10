package com.example.olskr.populationliblesson6.di;

import com.example.olskr.populationliblesson6.di.modules.ApiModule;
import com.example.olskr.populationliblesson6.di.modules.AppModule;
import com.example.olskr.populationliblesson6.di.modules.CacheModule;
import com.example.olskr.populationliblesson6.di.modules.CiceroneModule;
import com.example.olskr.populationliblesson6.di.modules.RepoModule;
import com.example.olskr.populationliblesson6.mvp.presenter.MainPresenter;
import com.example.olskr.populationliblesson6.ui.activity.MainActivity;

import dagger.Component;


import javax.inject.Singleton;

@Singleton
@Component(modules = { //@Component - та штука, благодаря, которой мы взаимодействуем с dagger
        AppModule.class,
        ApiModule.class,
        CacheModule.class,
        RepoModule.class,
        CiceroneModule.class
})
public interface AppComponent {
    void inject(MainPresenter mainPresenter);
    void inject(MainActivity mainActivity);
}
