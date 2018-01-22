package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/9/18.
 */

public class NameResponse extends ApiResponseBase {

    @Expose
    String name;

    public String getName() {
        return name;
    }
}
