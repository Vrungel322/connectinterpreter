package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class RequestResponse {
    @Expose
    long id;
    @Expose
    long created_at;
    @Expose
    long updated_at;
    @Expose
    String name;
    @Expose
    String status;

    public long getId() {
        return id;
    }

    public long getCreated_at() {
        return created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
