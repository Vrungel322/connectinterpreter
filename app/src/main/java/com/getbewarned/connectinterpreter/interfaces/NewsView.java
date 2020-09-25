package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.News;

public interface NewsView {

    void toggleNews(boolean isEmpty);

    void showError(String message);

    void navigateToNews(News news);
}
