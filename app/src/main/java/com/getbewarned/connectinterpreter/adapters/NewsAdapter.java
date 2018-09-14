package com.getbewarned.connectinterpreter.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.holders.NewsViewHolder;
import com.getbewarned.connectinterpreter.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<News> news = new ArrayList<>();

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.updateUI(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public void setNews(List<News> news) {
        this.news.clear();
        this.news.addAll(news);
        notifyDataSetChanged();
    }
}