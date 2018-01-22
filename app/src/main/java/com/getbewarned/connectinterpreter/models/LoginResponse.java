package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/9/18.
 */

public class LoginResponse extends ApiResponseBase {

    @Expose
    String name;

    @Expose
    long milliseconds;

    @Expose
    String auth_token;

    public String getName() {
        return name;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public String getAuthToken() {
        return auth_token;
    }
}
