package com.example.olskr.populationliblesson6.mvp.presenter;

import android.annotation.SuppressLint;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.olskr.populationliblesson6.mvp.model.entity.Repository;
import com.example.olskr.populationliblesson6.mvp.model.entity.User;
import com.example.olskr.populationliblesson6.mvp.model.repo.UsersRepo;
import com.example.olskr.populationliblesson6.mvp.presenter.list.IRepoListPresenter;
import com.example.olskr.populationliblesson6.mvp.view.MainView;
import com.example.olskr.populationliblesson6.mvp.view.item.RepoItemView;
import com.example.olskr.populationliblesson6.navigation.Screens;

import io.reactivex.Scheduler;
import io.reactivex.subjects.PublishSubject;

import ru.terrakok.cicerone.Router;
import timber.log.Timber;

import javax.inject.Inject;

@InjectViewState
//чтобы Посторить MVP модель на Moxy наследуемся от MvpPresenter<MainView> - класс Moxy, MainView - наш интерфейс "представления"
public class MainPresenter extends MvpPresenter<MainView> {

    public class RepoListPresenter implements IRepoListPresenter { //презентер для списка
        PublishSubject<RepoItemView> clickSubject = PublishSubject.create();

        @Override
        public PublishSubject<RepoItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public void bindView(RepoItemView view) {
            Repository repository = user.getRepos().get(view.getPos());
            view.setTitle(repository.getName());
        }

        @Override
        public int getRepoCount() {
            return user == null || user.getRepos() == null ? 0 : user.getRepos().size();
        }
    }

    @Inject Router router; //подключаем ротер от Cicerone
    @Inject //c помощью Dagger сделали зависимость(тоже самое что  и "new UsersRepo()")
    UsersRepo userRepo;

    private Scheduler mainThreadScheduler;
    public RepoListPresenter repoListPresenter = new RepoListPresenter();
    private User user;

    public MainPresenter(Scheduler scheduler, String arg) {
        this.mainThreadScheduler = scheduler;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadInfo();
    }

    public void onBackPressed() { //методя для возврашения назад с экрана(используется во фрагменте например
        router.exit(); //вернуться назад
        //router.backTo(new Screens.MainScreen("asd")); // вернуться на конкретный экран (вписать вместо...)
    }

    //допустим при нажатии на кпопку мы говорим роутеру, чтобы перешел на конкретный экран
    //можем совершать навигацию, необрашаясь ни к каким фрагмент менеджерам(Cicerone его оборачивает)
    public void someButtonClick(){
        router.navigateTo(new Screens.MainScreen("asd"));
    }

    @SuppressLint("CheckResult")
    public void loadInfo() {
        getViewState().showLoading();
        userRepo.getUser("googlesamples")
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .observeOn(mainThreadScheduler)
                            .subscribe(repositories -> {
                                this.user.setRepos(repositories);
                                getViewState().hideLoading();
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                            });


                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                    getViewState().hideLoading();
                    getViewState().showError(throwable.getMessage());
                });
    }
}
