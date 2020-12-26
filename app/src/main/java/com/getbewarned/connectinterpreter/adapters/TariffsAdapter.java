package com.getbewarned.connectinterpreter.adapters;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    private boolean hasDiscount;

    public TariffsAdapter(TariffClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setHasDiscount(boolean b) {
        hasDiscount = b;
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
        return new TariffViewHolder(view, hasDiscount);
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

    boolean hasDiscount;
    TextView tvTariffName;
    ImageView ivSelectionIndicator;
    TextView tvTariffPrice;
    TextView tvTariffWithoutPrice;
    TextView tvTariffDiscountPrice;
    TextView tvTariffMinutes;
    CardView cardView;


    public TariffViewHolder(View itemView, boolean hasDiscount) {
        super(itemView);
        this.hasDiscount = hasDiscount;
        tvTariffName = itemView.findViewById(R.id.tv_tariff_name);
        ivSelectionIndicator = itemView.findViewById(R.id.iv_selection_indicator);
        tvTariffPrice = itemView.findViewById(R.id.tv_tariff_price);
        tvTariffWithoutPrice = itemView.findViewById(R.id.tv_tariff_price_without_discount);
        tvTariffDiscountPrice = itemView.findViewById(R.id.tv_tariff_price_discount);
        tvTariffMinutes = itemView.findViewById(R.id.tv_tariff_minutes);
        cardView = itemView.findViewById(R.id.card);
    }


    public void updateUI(TariffItem item, @Nullable TariffItem selectedItem) {

        Drawable selectedColorBackground = cardView.getResources().getDrawable(R.drawable.tariff_selected_background);
        Drawable unselectedColorBackground = cardView.getResources().getDrawable(R.drawable.tariff_unselected_background);

        tvTariffName.setText(item.getTariffName());
        tvTariffPrice.setText(item.getTariffPrice() + item.getSign());
        tvTariffWithoutPrice.setText(item.getTariffPrice() + item.getSign());
        tvTariffDiscountPrice.setText(item.getDiscountPrice() + item.getSign());
        tvTariffMinutes.setText(String.valueOf(item.getTariffMinutes() + " мин."));
        tvTariffWithoutPrice.setPaintFlags(tvTariffWithoutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (hasDiscount) {
            tvTariffPrice.setVisibility(View.GONE);
            tvTariffWithoutPrice.setVisibility(View.VISIBLE);
            tvTariffDiscountPrice.setVisibility(View.VISIBLE);
        }else{
            tvTariffPrice.setVisibility(View.VISIBLE);
            tvTariffWithoutPrice.setVisibility(View.GONE);
            tvTariffDiscountPrice.setVisibility(View.GONE);
        }

        if (selectedItem != null) {
            boolean isSelected = item.getTariffId().equals(selectedItem.getTariffId());
            if (isSelected) {
                cardView.setBackground(selectedColorBackground);
                ivSelectionIndicator.setVisibility(View.VISIBLE);

            } else {
                cardView.setBackground(unselectedColorBackground);
                ivSelectionIndicator.setVisibility(View.INVISIBLE);
            }
        } else {
            cardView.setBackground(unselectedColorBackground);
            cardView.setBackground(selectedColorBackground);
            ivSelectionIndicator.setVisibility(View.INVISIBLE);
        }
    }


}