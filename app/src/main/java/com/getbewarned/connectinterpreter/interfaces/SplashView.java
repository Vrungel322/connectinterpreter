package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 1/24/18.
 */

public interface SplashView {
    void navigateToLogin();

    void navigateToApp();

    Context getContext();

    void showUpdateAlert(String version, boolean required);

    void showNewVersionInfo(String description);
}
