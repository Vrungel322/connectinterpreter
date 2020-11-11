package com.getbewarned.connectinterpreter.models;

public class TariffItem {
    private String tariffName;
    private String tariffPrice;
    private String sign;
    private int tariffMinutes;
    private String tariffId;
    private String currency;

    public TariffItem(String tariffName, String tariffPrice, int tariffMinutes, String tariffId, String sign, String currency) {
        this.tariffName = tariffName;
        this.tariffPrice = tariffPrice;
        this.tariffMinutes = tariffMinutes;
        this.tariffId = tariffId;
        this.currency = currency;
        this.sign = sign;
    }


    public String getTariffName() {
        return tariffName;
    }

    public String getTariffPrice() {
        return tariffPrice;
    }

    public String getSign() {
        return sign;
    }

    public int getTariffMinutes() {
        return tariffMinutes;
    }

    public String getTariffId() {
        return tariffId;
    }

    public String getCurrency() {
        return currency;
    }
}
