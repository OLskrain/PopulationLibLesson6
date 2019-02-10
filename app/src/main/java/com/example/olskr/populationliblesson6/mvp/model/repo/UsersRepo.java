package com.example.olskr.populationliblesson6.mvp.model.repo;

import com.example.olskr.populationliblesson6.NetworkStatus;
import com.example.olskr.populationliblesson6.mvp.model.api.IDataSource;
import com.example.olskr.populationliblesson6.mvp.model.cache.ICache;
import com.example.olskr.populationliblesson6.mvp.model.entity.Repository;
import com.example.olskr.populationliblesson6.mvp.model.entity.User;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


import java.util.List;

public class UsersRepo { //класс посредник между Presenter и Retrofit (нужно для тестирования)
    private ICache cache;
    private IDataSource dataSource;

    //Кеш лучше грузить через конструктор,а не через метод напрямую
    public UsersRepo(ICache cache, IDataSource dataSource) {
        this.cache = cache;
        this.dataSource = dataSource;
    }

    //метод , который или берет юзера из инета, если его нет, пытается прочитать из кеша или кидает ошибку
    public Single<User> getUser(String username) {
        if (NetworkStatus.isOnline()) { //если есть сеть
            return dataSource.getUser(username).subscribeOn(Schedulers.io())
                    .map(user -> {
                        cache.putUser(user); //пытаемся вытянуть юзера из инета
                        return user;
                    });
        } else {
            return cache.getUser(username); //если инета нет, то смотрим кеш
        }
    }

    //тоже самое для репозитория
    public Single<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) { //если есть сеть
            return dataSource.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io())
                    .map(repos -> {
                        cache.putUserRepos(user, repos); //берем репозиторий из инета
                        return repos;
                    });
        } else {
            return cache.getUserRepos(user); //если инета нет, смотрим кеш
        }
    }
}
