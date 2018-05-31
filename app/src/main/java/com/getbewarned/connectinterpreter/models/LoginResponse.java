package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/9/18.
 */

public class LoginResponse extends ApiResponseBase {

    @Expose
    String name;

    @Expose
    long seconds;

    @Expose
    boolean unlim;

    @Expose
    long active_till;

    @Expose
    String auth_token;

    @Expose
    Boolean is_utog;

    @Expose
    Boolean is_ukrainian;

    @Expose
    Boolean first_time;

    public String getName() {
        return name;
    }

    public long getSeconds() {
        return seconds;
    }

    public boolean isUnlim() {
        return unlim;
    }

    public long getActiveTill() {
        return active_till;
    }

    public String getAuthToken() {
        return auth_token;
    }

    public Boolean isUtog() {
        return is_utog;
    }

    public Boolean isUkrainian() {
        return is_ukrainian;
    }

    public Boolean getFirstTime() {
        return first_time;
    }
}
