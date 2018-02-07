package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class CountryResponse {
    @Expose
    String name;

    @Expose
    String code;

    public Country toCountry() {
        return  new Country(name, code);
    }

}
