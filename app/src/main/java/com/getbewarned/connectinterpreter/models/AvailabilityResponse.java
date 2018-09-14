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

    @Expose
    boolean is_utog;

    @Expose
    boolean is_utog_available;

    @Expose
    String region;

    @Expose
    String name;

    @Expose
    String phone;


    public long getSeconds() {
        return seconds;
    }

    public boolean isUnlim() {
        return unlim;
    }

    public long getActiveTill() {
        return active_till;
    }

    public boolean isUtog() {
        return is_utog;
    }

    public boolean isUtogAvailable() {
        return is_utog_available;
    }

    public String getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

