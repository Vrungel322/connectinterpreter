package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 10/11/17.
 */

public interface MainView {

    void showLeftTime(String leftTime);

    void toggleCallAvailability(boolean available);

    void showChecking();

    Context getContext();

    void navigateToCall();

    void showError(String message);

    void requestPermissions();

    void askForName(String name);

    void updateUserName(String name);

    void askForReason();

    void navigateToCallFor(String reason);

}
