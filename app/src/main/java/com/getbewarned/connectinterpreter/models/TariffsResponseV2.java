package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TariffsResponseV2 extends ApiResponseBase {
    @Expose
    List<TariffResponse> tariffs;

    @Expose
    boolean hasDiscount;

    @Expose
    String sign;

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public String getSign() {
        return sign;
    }

    public List<TariffResponse> getTariffs() {
        return tariffs;
    }
}


