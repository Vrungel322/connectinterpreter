package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/24/18.
 */

public class AppVersionResponse extends ApiResponseBase {

    @Expose
    String version;
    @Expose
    int build_number;
    @Expose
    boolean update_required;
    @Expose
    String description;


    public String getVersion() {
        return version;
    }

    public int getBuildNumber() {
        return build_number;
    }

    public boolean isUpdateRequired() {
        return update_required;
    }

    public String getDescription() {
        return description;
    }
}
