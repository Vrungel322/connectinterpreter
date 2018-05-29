package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.Request;

public interface RequestsView {

    void showError(String message);

    void toggleListVisibility(boolean visible);

    void openImagePicker();

    void openVideoPicker();

    void goToRequest(Request request);

    void showLoading();

    void hideLoading();
}
