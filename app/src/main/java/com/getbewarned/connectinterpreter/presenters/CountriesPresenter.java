package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;

import com.getbewarned.connectinterpreter.adapters.CountriesAdapter;
import com.getbewarned.connectinterpreter.interfaces.CountriesReceived;
import com.getbewarned.connectinterpreter.interfaces.CountriesView;
import com.getbewarned.connectinterpreter.interfaces.OnCountryClicked;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.ProxyManager;
import com.getbewarned.connectinterpreter.models.CountriesResponse;
import com.getbewarned.connectinterpreter.models.Country;
import com.getbewarned.connectinterpreter.models.CountryResponse;

import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by artycake on 2/7/18.
 */

public class CountriesPresenter implements Presenter {

    private CountriesAdapter adapter;
    private CountriesView view;
    private NetworkManager networkManager;
    private ProxyManager proxyManager;

    public CountriesPresenter(final CountriesView view) {
        this.view = view;
        adapter = new CountriesAdapter();
        networkManager = new NetworkManager(view.getContext());
        proxyManager = (ProxyManager) ProxySelector.getDefault();

        adapter.setOnCountryClicked(new OnCountryClicked() {
            @Override
            public void onCLicked(Country country) {
                view.returnWithCountry(country);
            }
        });

    }

    @Override
    public void onCreate(Bundle extras) {
        networkManager.getCountries(new CountriesReceived() {
            @Override
            public void onCountriesReceived(CountriesResponse response) {
                List<Country> countries = new ArrayList<>();
                for (CountryResponse country : response.getCountries()) {
                    countries.add(country.toCountry());
                }
                adapter.setCountries(countries);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void searchQueryChanged(String query) {
        adapter.search(query);
    }

    public CountriesAdapter getAdapter() {
        return adapter;
    }
}
