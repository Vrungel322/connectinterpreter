package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ReasonsResponse extends ApiResponseBase {
    @Expose
    List<ReasonResponse> reasons;

    public List<ReasonResponse> getReasons() {
        return reasons;
    }
}
