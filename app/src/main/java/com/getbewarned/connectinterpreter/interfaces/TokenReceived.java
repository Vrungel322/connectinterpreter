package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.TokenResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface TokenReceived {
    void onTokenReceived(TokenResponse response);

    void onErrorReceived(Error error);
}
