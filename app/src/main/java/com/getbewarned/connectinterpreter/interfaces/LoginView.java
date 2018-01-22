package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 1/9/18.
 */

public interface LoginView {

    void showPasswordAndLoginBtn();

    void updateHint(String message);

    void toggleEnabledRequestBtn(boolean enabled);

    Context getContext();

    void setCode(String code);

    void toggleEnabledLoginBtn(boolean enabled);

    void navigateToApp();

    void showError(String message);
}
