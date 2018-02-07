package com.getbewarned.connectinterpreter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.Country;

/**
 * Created by artycake on 2/7/18.
 */

public class CountryViewHolder extends RecyclerView.ViewHolder {

    private TextView countryName;
    private TextView phoneCode;

    public CountryViewHolder(View itemView) {
        super(itemView);
        countryName = itemView.findViewById(R.id.country_name);
        phoneCode = itemView.findViewById(R.id.phone_code);
    }

    public void updateUI(Country country) {
        countryName.setText(country.getName());
        phoneCode.setText(country.getCode());
    }
}
