package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

import com.getbewarned.connectinterpreter.models.Country;

/**
 * Created by artycake on 1/9/18.
 */

public interface LoginView {

    Context getContext();

    void toggleEnabledLoginBtn(boolean enabled);

    void showError(String message);

    void navigateToConfirmation(String phone);

    void setCountry(Country country);

    void confirmPhone(String phone);
}
