package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class TariffResponse {
    @Expose
    String name;
    @Expose
    String id;
    @Expose
    String price;
    @Expose
    int minutes;
    @Expose
    String currency_sign;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getCurrencySign() {
        return currency_sign;
    }
}
