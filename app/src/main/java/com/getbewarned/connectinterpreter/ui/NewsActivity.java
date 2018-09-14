package com.getbewarned.connectinterpreter.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.NewsView;
import com.getbewarned.connectinterpreter.presenters.NewsPresenter;

public class NewsActivity extends AppCompatActivity implements NewsView {

    RecyclerView newsList;
    TextView emptyNews;
    NewsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.drawer_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newsList = findViewById(R.id.news_list);
        emptyNews = findViewById(R.id.empty_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        presenter = new NewsPresenter(this, this);
        presenter.onCreate(getIntent().getExtras());

        newsList.setAdapter(presenter.getAdapter());


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
