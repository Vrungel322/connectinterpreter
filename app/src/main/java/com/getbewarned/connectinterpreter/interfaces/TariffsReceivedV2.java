package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;

/**
 * Created by artycake on 1/17/18.
 */

public interface TariffsReceivedV2 {
    void onTariffsReceived(TariffsResponseV2 response);

    void onErrorReceived(Error error);
}
