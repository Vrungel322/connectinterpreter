package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.CountriesResponse;

/**
 * Created by artycake on 2/7/18.
 */

public interface CountriesReceived {

    void onCountriesReceived(CountriesResponse response);

    void onErrorReceived(Error error);
}
