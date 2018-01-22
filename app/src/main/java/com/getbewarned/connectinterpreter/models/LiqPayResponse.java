package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/17/18.
 */

public class LiqPayResponse extends ApiResponseBase {
    @Expose
    String data;
    @Expose
    String signature;

    public String getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }
}
