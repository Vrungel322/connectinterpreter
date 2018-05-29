package com.getbewarned.connectinterpreter.interfaces;

import android.view.View;

import java.util.Date;

public interface GroupSessionView {

    void showError(String message);

    void showSessionInfo(String sessionName, Date sessionDate, boolean connected);

    void updateInterpreterView(View interpreterView);

    void showReconnect();

    void toggleLoading(boolean isLoading);

    void updateAskerView(View askerView);

    void toggleAsking(boolean asking);

    void toggleAskButtonVisibility(boolean visible);

    void leave();

    void runOnUi(Runnable action);
}
