package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by artycake on 1/17/18.
 */

public class TariffsResponse extends ApiResponseBase {
    @Expose
    List<TariffResponse> tariffs;

    public List<TariffResponse> getTariffs() {
        return tariffs;
    }
}


