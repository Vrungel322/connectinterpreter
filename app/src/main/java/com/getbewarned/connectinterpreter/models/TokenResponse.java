package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 10/4/17.
 */

public class TokenResponse extends ApiResponseBase {
    @Expose
    String token;

    @Expose
    String session_id;

    @Expose
    String api_key;

    @Expose
    long max_seconds;


    public String getToken() {
        return token;
    }

    public String getSessionId() {
        return session_id;
    }

    public String getApiKey() {
        return api_key;
    }

    public long getMaxSeconds() {
        return max_seconds;
    }
}
