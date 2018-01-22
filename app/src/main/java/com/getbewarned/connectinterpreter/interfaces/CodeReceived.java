package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;

/**
 * Created by artycake on 10/11/17.
 */

public interface CodeReceived {
    void onCodeReceived(ApiResponseBase response);

    void onErrorReceived(Error error);
}
