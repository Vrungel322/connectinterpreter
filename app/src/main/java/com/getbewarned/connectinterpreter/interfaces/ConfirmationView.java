package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 1/24/18.
 */

public interface ConfirmationView {
    void navigateToApp();

    Context getContext();

    void showError(String message);

    void toggleEnabledRequestBtn(boolean enabled);

    void setCode(String code);

    void navigateBack();

    void toggleEnabledLoginBtn(boolean enabled);

    void showNumber(String phone);

    void showHelpRequested();
}
