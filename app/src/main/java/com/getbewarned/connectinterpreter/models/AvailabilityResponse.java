package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/9/18.
 */

public class AvailabilityResponse extends ApiResponseBase {

    @Expose
    long seconds;

    @Expose
    boolean unlim;

    @Expose
    long active_till;


    public long getSeconds() {
        return seconds;
    }

    public boolean isUnlim() {
        return unlim;
    }

    public long getActiveTill() {
        return active_till;
    }
}

