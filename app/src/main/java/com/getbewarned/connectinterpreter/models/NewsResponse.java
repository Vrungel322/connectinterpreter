package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class NewsResponse extends ApiResponseBase {
    @Expose
    List<OneNewsResponse> news;

    public List<OneNewsResponse> getNews() {
        return news;
    }
}
