package com.getbewarned.connectinterpreter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.holders.CountryViewHolder;
import com.getbewarned.connectinterpreter.interfaces.OnCountryClicked;
import com.getbewarned.connectinterpreter.models.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artycake on 2/7/18.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountryViewHolder> {

    private final List<Country> countries = new ArrayList<>();
    private final List<Country> allCountries = new ArrayList<>();
    private OnCountryClicked onCountryClicked;

    @Override
    public CountryViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCountryClicked == null) {
                    return;
                }
                onCountryClicked.onCLicked(countries.get(holder.getAdapterPosition()));
            }
        });
        holder.updateUI(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void setCountries(List<Country> countries) {
        this.countries.addAll(countries);
        this.allCountries.addAll(countries);
        notifyDataSetChanged();
    }

    public void search(String query) {
        this.countries.clear();
        if (query.isEmpty()) {
            this.countries.addAll(this.allCountries);
            notifyDataSetChanged();
            return;
        }
        for (Country country : this.allCountries) {
            if (country.getName().toLowerCase().contains(query.toLowerCase())) {
                this.countries.add(country);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnCountryClicked(OnCountryClicked onCountryClicked) {
        this.onCountryClicked = onCountryClicked;
    }
}
