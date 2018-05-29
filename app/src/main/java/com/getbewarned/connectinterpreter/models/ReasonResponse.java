package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class ReasonResponse {
    @Expose
    String slug;
    @Expose
    String label;

    public String getSlug() {
        return slug;
    }

    public String getLabel() {
        return label;
    }
}
