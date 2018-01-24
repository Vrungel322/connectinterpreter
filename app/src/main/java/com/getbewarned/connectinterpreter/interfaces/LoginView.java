package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 1/9/18.
 */

public interface LoginView {

    Context getContext();

    void toggleEnabledLoginBtn(boolean enabled);

    void showError(String message);

    void navigateToConfirmation(String phone);
}
