package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/17/18.
 */

public class CreateYandexPaymentResponse extends ApiResponseBase {
    @Expose
    String confirmation;
    @Expose
    String id;

    public String getConfirmation() {
        return confirmation;
    }

    public String getId() {
        return id;
    }
}


