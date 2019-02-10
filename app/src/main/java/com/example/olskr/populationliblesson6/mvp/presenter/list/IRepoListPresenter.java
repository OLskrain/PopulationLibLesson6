package com.example.olskr.populationliblesson6.mvp.presenter.list;

import com.example.olskr.populationliblesson6.mvp.view.item.RepoItemView;

import io.reactivex.subjects.PublishSubject;


public interface IRepoListPresenter
{
    PublishSubject<RepoItemView> getClickSubject();
    void bindView(RepoItemView rowView);
    int getRepoCount();
}
