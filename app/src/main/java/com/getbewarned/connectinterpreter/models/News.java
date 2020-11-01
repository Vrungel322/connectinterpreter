package com.getbewarned.connectinterpreter.models;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Serializable {

    String title;
    String text;
    Date date;
    @Nullable
    String videoUrl;

    public News(String title, String text, long dateTime, @Nullable String videoUrl) {
        this.title = title;
        this.text = text;
        this.date = new Date(dateTime * 1000); // dateTime is sec, dateTime *1000 is millis
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

    public String dateFormatted() {
        DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        return format.format(getDate());
    }
}
