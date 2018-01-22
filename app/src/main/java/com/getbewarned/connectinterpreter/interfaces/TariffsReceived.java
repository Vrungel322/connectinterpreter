package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TokenResponse;

/**
 * Created by artycake on 1/17/18.
 */

public interface TariffsReceived {
    void onTariffsReceived(TariffsResponse response);

    void onErrorReceived(Error error);
}
