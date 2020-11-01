package com.getbewarned.connectinterpreter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.TariffClickListener;
import com.getbewarned.connectinterpreter.models.TariffItem;

import java.util.ArrayList;
import java.util.List;

public class TariffsAdapter extends RecyclerView.Adapter<TariffViewHolder> {
    public TariffItem selectedItem = null;
    private List<TariffItem> items = new ArrayList<>();
    private TariffClickListener clickListener;

    public TariffsAdapter(TariffClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setItems(List<TariffItem> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tariff, parent, false);
        return new TariffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
        final TariffItem item = items.get(position);
        holder.updateUI(item, selectedItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == selectedItem) {
                    selectedItem = null;
                } else {
                    selectedItem = item;
                }
                notifyDataSetChanged();
                clickListener.onTariffClick(selectedItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class TariffViewHolder extends RecyclerView.ViewHolder {
    TextView tvTariffName;
    ImageView ivSelectionIndicator;
    TextView tvTariffPrice;
    TextView tvTariffMinutes;
    LinearLayout llUnselectedTint;

    public TariffViewHolder(View itemView) {
        super(itemView);
        tvTariffName = itemView.findViewById(R.id.tv_tariff_name);
        ivSelectionIndicator = itemView.findViewById(R.id.iv_selection_indicator);
        tvTariffPrice = itemView.findViewById(R.id.tv_tariff_price);
        tvTariffMinutes = itemView.findViewById(R.id.tv_tariff_minutes);
        llUnselectedTint = itemView.findViewById(R.id.ll_unselected_tint);
    }


    public void updateUI(TariffItem item, @Nullable TariffItem selectedItem) {
        tvTariffName.setText(item.tariffName);
        tvTariffPrice.setText(item.tariffPrice);
        tvTariffMinutes.setText(item.tariffMinutes);

        if (selectedItem != null) {
            boolean isSelected = item.tariffId.equals(selectedItem.tariffId);
            if (isSelected) {
                llUnselectedTint.setVisibility(View.GONE);
                ivSelectionIndicator.setVisibility(View.VISIBLE);
            } else {
                llUnselectedTint.setVisibility(View.VISIBLE);
                ivSelectionIndicator.setVisibility(View.GONE);
            }
        } else {
            llUnselectedTint.setVisibility(View.GONE);
            ivSelectionIndicator.setVisibility(View.GONE);
        }
    }

}