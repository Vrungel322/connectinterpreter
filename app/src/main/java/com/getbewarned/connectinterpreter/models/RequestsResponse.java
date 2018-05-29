package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class RequestsResponse extends ApiResponseBase {

    @Expose
    List<RequestResponse> requests;

    public List<RequestResponse> getRequests() {
        return requests;
    }
}

