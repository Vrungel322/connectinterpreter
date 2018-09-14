package com.getbewarned.connectinterpreter.models;

import android.support.annotation.Nullable;

import java.net.URL;
import java.util.Date;

public class News {

    String title;
    String text;
    Date date;
    @Nullable String videoUrl;

    public News(String title, String text, long dateTime, @Nullable  String videoUrl) {
        this.title = title;
        this.text = text;
        this.date = new Date(dateTime);
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
