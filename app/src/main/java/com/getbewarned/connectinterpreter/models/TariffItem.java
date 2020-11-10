package com.getbewarned.connectinterpreter.models;

public class TariffItem {
    private String tariffName;
    private String tariffPrice;
    private String sign;
    private int tariffMinutes;
    private String tariffId;

    public TariffItem(String tariffName, String tariffPrice, int tariffMinutes, String tariffId, String sign) {
        this.tariffName = tariffName;
        this.tariffPrice = tariffPrice;
        this.tariffMinutes = tariffMinutes;
        this.tariffId = tariffId;
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
}
