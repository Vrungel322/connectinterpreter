package com.getbewarned.connectinterpreter.models;

public class TariffItem {
    public String tariffName;
    public String tariffPrice;
    public String tariffMinutes;
    public String tariffId;

    public TariffItem(String tariffName, String tariffPrice, String tariffMinutes, String tariffId) {
        this.tariffName = tariffName;
        this.tariffPrice = tariffPrice;
        this.tariffMinutes = tariffMinutes;
        this.tariffId = tariffId;
    }
}
