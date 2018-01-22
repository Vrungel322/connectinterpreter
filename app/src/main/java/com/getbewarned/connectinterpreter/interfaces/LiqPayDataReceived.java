package com.getbewarned.connectinterpreter.interfaces;


import com.getbewarned.connectinterpreter.models.LiqPayResponse;

/**
 * Created by artycake on 1/17/18.
 */

public interface LiqPayDataReceived {
    void onDataReceived(LiqPayResponse response);

    void onErrorReceived(Error error);
}
