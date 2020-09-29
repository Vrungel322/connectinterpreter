package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.Request;

public interface RequestView {

    void goBack();

    void updateUi(Request request);

    void hideSendButtons();

    void showVideoPreview(String url);

    void showImagePreview(String url);

    void showLoading();

    void hideLoading();

    void scrollToBottom();
}
