package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class RequestMessageResponse {
    @Expose
    long id;
    @Expose
    long created_at;
    @Expose
    String type;
    @Expose
    String content;
    @Expose
    String thumbnail;
    @Expose
    String author_type;

    public long getId() {
        return id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorType() {
        return author_type;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
