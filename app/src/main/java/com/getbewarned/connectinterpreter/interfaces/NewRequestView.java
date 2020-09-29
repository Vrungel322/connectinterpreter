package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.Request;

public interface NewRequestView {

    void goToRequest(Request request);

    void showLoading();

    void hideLoading();

    void showError(String message);

}
