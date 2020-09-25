package com.getbewarned.connectinterpreter.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.News;

public class NewsDetail extends NoStatusBarActivity {
    static final String NEWS_KEY = "NEWS_KEY";

    private News news;

    private TextView title;
    private TextView content;
    private WebView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        news = (News) getIntent().getSerializableExtra(NEWS_KEY);

        // toolbar
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(news.dateFormatted());
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // content
        title = findViewById(R.id.news_title);
        content = findViewById(R.id.news_content);
        videoView = findViewById(R.id.video_view);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setWebChromeClient(new WebChromeClient());
        updateUI(news);
    }

    public void updateUI(News news) {
        title.setText(news.getTitle());
        content.setText(news.getText());

        if (news.getVideoUrl() == null) {
            videoView.setVisibility(View.GONE);
            videoView.reload();
        } else {
            String iframeTmp = "<iframe width=\"100%%\" height=\"100%%\" frameborder=\"0\" allowfullscreen src=\"%s\"></iframe>";
            String iframe = String.format(iframeTmp, news.getVideoUrl());
            videoView.loadData(iframe, "text/html", "utf-8");
            videoView.setVisibility(View.VISIBLE);
        }
    }
}
