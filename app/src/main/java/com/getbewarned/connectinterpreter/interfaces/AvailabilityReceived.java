package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.AvailabilityResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface AvailabilityReceived {
    void onAvailabilityReceived(AvailabilityResponse response);

    void onErrorReceived(Error error);
}
