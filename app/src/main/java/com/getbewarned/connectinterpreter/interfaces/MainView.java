package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

import com.getbewarned.connectinterpreter.models.Reason;
import com.getbewarned.connectinterpreter.models.TariffResponse;

import java.util.List;

/**
 * Created by artycake on 10/11/17.
 */

public interface MainView {

    void showLeftTime(String leftTime);

    void toggleCallAvailability(boolean available, boolean isUtog);

    void showChecking();

    void showError(String message);

    void showErrorNewUI(String message);

    void requestPermissions();

    void requestLiqPayPermissions();

    void askForName(String name);

    void updateUserName(String name);

    void askForReason(List<Reason> reasons);

    void navigateToCallWith(String token, String sessionId, String apiKey, long maxSeconds);

    void navigateToLogin();

    void showDateTill(String dateTill);

    void toggleBuyUnlimEnabled(boolean enabled);

    void showTariffsSelector(List<TariffResponse> tariffs);

    void askAboutUtog();

    void navigateToUtog();

    void askAboutLastCall();

}
