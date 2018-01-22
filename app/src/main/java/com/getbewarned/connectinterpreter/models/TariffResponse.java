package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class TariffResponse {
    @Expose
    String name;
    @Expose
    String id;
    @Expose
    double price;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}
