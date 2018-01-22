package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.LoginResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface LoginComplete {
    void onLoginComplete(LoginResponse response);

    void onErrorReceived(Error error);
}
