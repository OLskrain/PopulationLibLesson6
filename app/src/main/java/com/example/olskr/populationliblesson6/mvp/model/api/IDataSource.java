package com.example.olskr.populationliblesson6.mvp.model.api;

import com.example.olskr.populationliblesson6.mvp.model.entity.Repository;
import com.example.olskr.populationliblesson6.mvp.model.entity.User;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;


import java.util.List;

//КАК ПРАВИЛЬНО: у нас есть какая то точка ддоступа https://api.github.com/users/googlesample
//и для каждой точки доступа должен быть 1 интерфейс (1)
public interface IDataSource { //(1)
    @GET("users/{user}") //Get - тип запроса
    Single<User> getUser(@Path("user") String userName); //запрос будет возврашать singl c user-ом

    @GET("users/{user}/repos")
    Single<List<Repository>> getUserRepos(@Path("user") String userName);

}
