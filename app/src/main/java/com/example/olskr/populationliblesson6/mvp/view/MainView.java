package com.example.olskr.populationliblesson6.mvp.view;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

//c помошью аннотации пишем стратегию - @StateStrategyType(value = AddToEndSingleStrategy.class) - которая говорит нам :
// "Запомни последнее действие из одинаковых
//ТАк же у нас другие стратегии
@StateStrategyType(AddToEndSingleStrategy.class) //чтобы Посторить MVP модель на Moxy наследуемся от MvpView - класс Moxy
public interface MainView extends MvpView
{
    void showAvatar(String avatarUrl);
    void showError(String message);
    void setUsername(String username);
    void showLoading();
    void hideLoading();

    void updateRepoList();
}
