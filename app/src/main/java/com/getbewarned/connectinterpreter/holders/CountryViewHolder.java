package com.getbewarned.connectinterpreter.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.Country;

/**
 * Created by artycake on 2/7/18.
 */

public class CountryViewHolder extends RecyclerView.ViewHolder {

    private final TextView countryName;
    private final TextView phoneCode;

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
