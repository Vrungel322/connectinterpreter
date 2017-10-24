package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.OpenTokTokenResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface TokenReceived {
    void onTokenReceived(OpenTokTokenResponse response);

    void onErrorReceived(Error error);
}
