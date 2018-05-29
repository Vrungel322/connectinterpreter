package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.RequestsResponse;

/**
 * Created by artycake on 1/17/18.
 */

public interface RequestsReceived {
    void onRequestsReceived(RequestsResponse response);

    void onErrorReceived(Error error);
}
