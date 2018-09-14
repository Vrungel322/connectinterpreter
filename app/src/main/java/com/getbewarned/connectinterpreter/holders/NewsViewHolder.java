package com.getbewarned.connectinterpreter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView date;
    TextView content;
    WebView videoView;

    public NewsViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.news_title);
        date = itemView.findViewById(R.id.news_date);
        content = itemView.findViewById(R.id.news_content);
        videoView = itemView.findViewById(R.id.video_view);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setWebChromeClient(new WebChromeClient());
    }


    public void updateUI(News news) {
        title.setText(news.getTitle());
        content.setText(news.getText());
        DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        date.setText(format.format(news.getDate()));

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
