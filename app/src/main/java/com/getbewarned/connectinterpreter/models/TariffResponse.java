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
    String discountPrice;
    @Expose
    int minutes;
    @Expose
    String currency_sign;

    @Expose
    String currency;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getCurrencySign() {
        return currency_sign;
    }

    public String getCurrency() {
        return currency;
    }
}
