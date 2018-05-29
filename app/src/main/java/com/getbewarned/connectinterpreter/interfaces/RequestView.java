package com.getbewarned.connectinterpreter.interfaces;

public interface RequestView {

    void goBack();

    void setTitle(String title);

    void hideSendButtons();

    void showVideoPreview(String url);

    void showImagePreview(String url);

    void showLoading();

    void hideLoading();

    void scrollToBottom();
}
