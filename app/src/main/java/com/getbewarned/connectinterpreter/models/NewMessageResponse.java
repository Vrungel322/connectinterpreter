package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class NewMessageResponse extends ApiResponseBase {

    @Expose
    RequestMessageResponse request_message;

    public RequestMessageResponse getRequestMessage() {
        return request_message;
    }
}
