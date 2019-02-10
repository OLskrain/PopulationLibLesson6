package com.example.olskr.populationliblesson6.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


import com.example.olskr.populationliblesson6.R;
import com.example.olskr.populationliblesson6.mvp.presenter.list.IRepoListPresenter;
import com.example.olskr.populationliblesson6.mvp.view.item.RepoItemView;
import com.jakewharton.rxbinding2.view.RxView;


public class RepoRVAdapter extends RecyclerView.Adapter<RepoRVAdapter.ViewHolder> { //адаптер для Recycle
    IRepoListPresenter presenter;

    public RepoRVAdapter(IRepoListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //подписываемся на клик по элементу clicks(holder.itemView). мепим в холдер map(obj -> holder) и отправляем в
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject());
        holder.pos = position; //говорим текушую позицию холдера
        presenter.bindView(holder); // и говорим, чтобы забилдир холдер(все происходит через интерфейс)
    }

    @Override
    public int getItemCount() {
        return presenter.getRepoCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RepoItemView {

        int pos = 0;

        @BindView(R.id.tv_title)
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public void setTitle(String title) {
            titleTextView.setText(title);
        }
    }
}
