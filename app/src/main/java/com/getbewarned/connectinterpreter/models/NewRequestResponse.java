package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class NewRequestResponse extends ApiResponseBase {
    @Expose
    RequestResponse request;
    @Expose
    RequestMessageResponse request_message;

    public RequestResponse getRequest() {
        return request;
    }

    public RequestMessageResponse getRequestMessage() {
        return request_message;
    }
}
