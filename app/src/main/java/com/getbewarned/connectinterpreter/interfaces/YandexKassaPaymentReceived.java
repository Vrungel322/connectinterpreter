package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.CreateYandexPaymentResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;

/**
 * Created by artycake on 1/17/18.
 */

public interface YandexKassaPaymentReceived {
    void onPaymentReceived(CreateYandexPaymentResponse response);

    void onErrorReceived(Error error);
}
