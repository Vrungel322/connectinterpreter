package com.getbewarned.connectinterpreter.models;

/**
 * Created by artycake on 2/7/18.
 */

public class Country {

    private String name;
    private String code;

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
