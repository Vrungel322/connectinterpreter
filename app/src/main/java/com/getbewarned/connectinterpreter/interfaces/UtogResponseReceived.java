package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.UtogResponse;

/**
 * Created by artycake on 2/2/18.
 */

public interface UtogResponseReceived {

    void onInfoReceived(UtogResponse response);

    void onErrorReceived(Error error);
}
