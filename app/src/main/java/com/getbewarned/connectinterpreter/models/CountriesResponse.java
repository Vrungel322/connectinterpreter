package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by artycake on 2/7/18.
 */

public class CountriesResponse extends ApiResponseBase {
    @Expose
    List<CountryResponse> countries;

    public List<CountryResponse> getCountries() {
        return countries;
    }
}


