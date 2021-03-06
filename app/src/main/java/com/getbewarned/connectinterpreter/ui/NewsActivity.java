package com.getbewarned.connectinterpreter.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.NewsView;
import com.getbewarned.connectinterpreter.models.News;
import com.getbewarned.connectinterpreter.presenters.NewsPresenter;

public class NewsActivity extends NoStatusBarActivity implements NewsView {

    RecyclerView newsList;
    TextView emptyNews;
    NewsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.drawer_news);
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        newsList = findViewById(R.id.news_list);
        emptyNews = findViewById(R.id.empty_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        presenter = new NewsPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());

        newsList.setAdapter(presenter.getAdapter());

        Analytics.getInstance().trackEvent(Events.EVENT_NEWS_OPENED);
    }

    @Override
    public void toggleNews(final boolean isEmpty) {
        if (isEmpty) {
            newsList.setVisibility(View.GONE);
            emptyNews.setVisibility(View.VISIBLE);
        } else {
            newsList.setVisibility(View.VISIBLE);
            emptyNews.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_global)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void navigateToNews(News news) {
        Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.NEWS_KEY, news);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
