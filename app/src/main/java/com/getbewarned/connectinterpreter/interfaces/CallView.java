package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;
import android.view.View;

/**
 * Created by artycake on 10/11/17.
 */

public interface CallView {

    void updateLeftTime(String leftTime);

    void updateCurrentCallDuration(String duration);

    Context getContext();

    void toggleEndCallButtonVisibility(boolean visible);

    void showIndicator(Long pleaseWaiteVideoDelayMillis);

    void hideIndicator();

    void showError(String message);

    void showErrorNewUI(String message);

    void navigateBack();

    void showSelfView(View view);

    void showInterpreterView(View view);

    void showOneMoreMessage(String message);

    void setMaxVolume();

    void resetVolume();
}
