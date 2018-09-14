package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class TariffResponse {
    @Expose
    String name;
    @Expose
    String id;
    @Expose
    String price;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }
}
