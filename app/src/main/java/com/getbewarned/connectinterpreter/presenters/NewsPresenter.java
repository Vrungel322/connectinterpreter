package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.adapters.NewsAdapter;
import com.getbewarned.connectinterpreter.adapters.OnNewsClick;
import com.getbewarned.connectinterpreter.interfaces.NewsReceived;
import com.getbewarned.connectinterpreter.interfaces.NewsView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.News;
import com.getbewarned.connectinterpreter.models.NewsResponse;
import com.getbewarned.connectinterpreter.models.OneNewsResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter implements Presenter {

    private NewsView newsView;
    NetworkManager networkManager;
    private NewsAdapter adapter;

    public NewsPresenter(final NewsView view, Context context) {
        this.newsView = view;
        UserManager userManager = new UserManager(context);
        networkManager = new NetworkManager(context);
        networkManager.setAuthToken(userManager.getUserToken());

        adapter = new NewsAdapter(new OnNewsClick() {

            @Override
            public void onNewsClick(News item) {
                view.navigateToNews(item);
            }
        });
    }

    public NewsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle extras) {
        networkManager.getNews(new NewsReceived() {
            @Override
            public void onDataReceived(NewsResponse response) {
                if (newsView == null) {
                    return;
                }
                List<News> news = new ArrayList<>();
                for (OneNewsResponse oneNewsResponse : response.getNews()) {
                    news.add(new News(oneNewsResponse.getTitle(), oneNewsResponse.getContent(), oneNewsResponse.getDatetime(), oneNewsResponse.getVideo()));
                }
                adapter.setNews(news);
                newsView.toggleNews(response.getNews().size() == 0);
            }

            @Override
            public void onErrorReceived(Error error) {
                if (newsView == null) {
                    return;
                }
                newsView.showError(error.getMessage());
            }
        });
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        newsView = null;
    }
}
