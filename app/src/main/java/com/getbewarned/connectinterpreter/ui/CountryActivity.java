package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.CountriesAdapter;
import com.getbewarned.connectinterpreter.interfaces.CountriesView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.models.Country;
import com.getbewarned.connectinterpreter.presenters.CountriesPresenter;

public class CountryActivity extends AppCompatActivity implements CountriesView {


    private CountriesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new CountriesPresenter(this);

        RecyclerView countryList = findViewById(R.id.country_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        countryList.setLayoutManager(layoutManager);
        countryList.setAdapter(presenter.getAdapter());

        EditText searchCountry = findViewById(R.id.search_country);
        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.searchQueryChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message) {
        if (isFinishing()) {
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void returnWithCountry(Country country) {
        Intent intent = new Intent();
        intent.putExtra("name", country.getName());
        intent.putExtra("code", country.getCode());
        setResult(RESULT_OK, intent);
        finish();
    }
}
