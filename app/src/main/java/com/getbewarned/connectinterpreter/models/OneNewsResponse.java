package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class OneNewsResponse {
    @Expose
    String title;
    @Expose
    String content;
    @Expose
    long datetime;
    @Expose
    String video;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getDatetime() {
        return datetime;
    }

    public String getVideo() {
        return video;
    }
}
