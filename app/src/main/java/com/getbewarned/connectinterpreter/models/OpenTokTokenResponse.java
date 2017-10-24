package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 10/4/17.
 */

public class OpenTokTokenResponse {
    @Expose
    String token;

    @Expose
    String session_id;

    @Expose
    String api_key;

    @Expose
    int http_status;

    public String getToken() {
        return token;
    }

    public String getSessionId() {
        return session_id;
    }

    public String getApiKey() {
        return api_key;
    }

    public int getHttpStatus() {
        return http_status;
    }
}
