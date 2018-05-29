package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class GroupSessionResponse extends ApiResponseBase {

    @Expose
    String event_name;

    @Expose
    long event_date;

    @Expose
    String token;

    @Expose
    String session_id;

    @Expose
    String api_key;

    @Expose
    boolean can_connect;

    @Expose
    int client_id;


    public String getEventName() {
        return event_name;
    }

    public long getEventDate() {
        return event_date;
    }

    public String getToken() {
        return token;
    }

    public String getSessionId() {
        return session_id;
    }

    public String getApiKey() {
        return api_key;
    }

    public boolean canConnect() {
        return can_connect;
    }

    public int getClient_id() {
        return client_id;
    }
}
