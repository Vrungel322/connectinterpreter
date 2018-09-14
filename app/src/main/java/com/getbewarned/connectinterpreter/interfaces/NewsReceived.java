package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.NewsResponse;

public interface NewsReceived {

    void onDataReceived(NewsResponse response);

    void onErrorReceived(Error error);
}