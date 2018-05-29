package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MessagesResponse extends ApiResponseBase {

    @Expose
    List<RequestMessageResponse> messages;

    public List<RequestMessageResponse> getMessages() {
        return messages;
    }
}
