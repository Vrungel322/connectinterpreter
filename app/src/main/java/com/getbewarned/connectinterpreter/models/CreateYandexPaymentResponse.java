package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

/**
 * Created by artycake on 1/17/18.
 */

public class CreateYandexPaymentResponse {

    @Expose
    Boolean status;
    @Expose
    Response response;

    public String getConfirmation() {
        return response.confirmation;
    }

    public String getId() {
        return response.id;
    }

    public Boolean getStatus() {
        return status;
    }

    static class Response {

        @Expose
        String confirmation;
        @Expose
        String id;

        public String getConfirmation() {
            return confirmation;
        }

        public String getId() {
            return id;
        }
    }
}


