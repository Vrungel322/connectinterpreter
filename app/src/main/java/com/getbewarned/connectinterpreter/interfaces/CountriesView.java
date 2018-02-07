package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

import com.getbewarned.connectinterpreter.models.Country;

/**
 * Created by artycake on 2/7/18.
 */

public interface CountriesView {

    Context getContext();

    void showError(String message);

    void returnWithCountry(Country country);
}
