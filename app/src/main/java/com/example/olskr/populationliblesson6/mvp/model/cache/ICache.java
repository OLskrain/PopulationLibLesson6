package com.example.olskr.populationliblesson6.mvp.model.cache;

import com.example.olskr.populationliblesson6.mvp.model.entity.Repository;
import com.example.olskr.populationliblesson6.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Single;


public interface ICache { //интерфейс кеша
    void putUser(User user);

    Single<User> getUser(String username);

    void putUserRepos(User user, List<Repository> repositories);

    Single<List<Repository>> getUserRepos(User user);
}
