package com.example.olskr.populationliblesson6.mvp.model.cache;

import com.example.olskr.populationliblesson6.mvp.model.entity.Repository;
import com.example.olskr.populationliblesson6.mvp.model.entity.User;
import com.example.olskr.populationliblesson6.mvp.model.entity.room.RoomRepository;
import com.example.olskr.populationliblesson6.mvp.model.entity.room.RoomUser;
import com.example.olskr.populationliblesson6.mvp.model.entity.room.db.UserDatabase;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


import java.util.ArrayList;
import java.util.List;

public class RoomCache implements ICache { //Рум кеш для данных из сети
    @Override
    public void putUser(User user) {
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()//узнаем есть ли там наш узер
                .findByLogin(user.getLogin());

        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
        }

        roomUser.setAvatarUrl(user.getAvatarUrl()); //обновляем аватар
        roomUser.setReposUrl(user.getReposUrl()); //обновляем репозиторйи

        UserDatabase.getInstance().getUserDao()
                .insert(roomUser); //сохранили юзера в кеш
    }

    @Override
    public Single<User> getUser(String username) { //если нет сети , то пытаемся прочитать из кеша
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                    .findByLogin(username);

            if (roomUser == null) { //если пользователя нет в кеше
                emitter.onError(new RuntimeException("No such user in cache")); //кидаем ошибку
            } else { //если он есть в кеше. то делаем  юзера, наполняя его из roomUsera - т.е из кеша
                emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public void putUserRepos(User user, List<Repository> repos) { //тоже самое для репозитория
        //(1)
        //если вдруг к нам пришел репозиторий а user мы не знаем , то создаем его
        RoomUser roomUser = UserDatabase.getInstance().getUserDao()
                .findByLogin(user.getLogin());

        //(2)
        if (roomUser == null) {
            roomUser = new RoomUser();
            roomUser.setLogin(user.getLogin());
            roomUser.setAvatarUrl(user.getAvatarUrl());
            roomUser.setReposUrl(user.getReposUrl());
            UserDatabase.getInstance()
                    .getUserDao()
                    .insert(roomUser);
        }//создали пользователя из того что пришло User user и записали его

        if (!repos.isEmpty()) { //если репозитории, которые пришли не пусты
            //записываем в список все репозитории юзера
            List<RoomRepository> roomRepositories = new ArrayList<>();
            for (Repository repository : repos) {
                RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                roomRepositories.add(roomRepository); //составляем репозитории
            }
//сохраняем репозитории в кеш
            UserDatabase.getInstance()
                    .getRepositoryDao()
                    .insert(roomRepositories);
        }
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) { //если нет сети, то читаем из кеша
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance() //проверяем есть ли пользователь
                    .getUserDao()
                    .findByLogin(user.getLogin());

            if(roomUser == null){ //если нет пользователя в кеше, то ошибка
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                //если пользователь есть, то получаем все его репозитории
                List<RoomRepository> roomRepositories = UserDatabase.getInstance().getRepositoryDao()
                        .getAll();

                List<Repository> repos = new ArrayList<>();
                for (RoomRepository roomRepository: roomRepositories){
                    repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                }

                emitter.onSuccess(repos);
            }
            //(Class<List<Repository>>)(Class)List.class - сначала лист привели к классу, а потом все привели к Class<List<Repository>>
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>)(Class)List.class);
    }
}
