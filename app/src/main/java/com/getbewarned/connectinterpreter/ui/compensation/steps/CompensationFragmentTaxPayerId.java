package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

public class CompensationFragmentTaxPayerId extends BaseCompensationStep {
    EditText etTaxPayerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_tax_payer_id, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaxPayerId = view.findViewById(R.id.et_tax_payer_id);

        etTaxPayerId.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    public void initData() {
        etTaxPayerId.setText(CompensationDataHolder.getInstance().taxPayerId);
        super.initData();
    }

    @Override
    public void storeData() {
        CompensationDataHolder.getInstance().taxPayerId = etTaxPayerId.getText().toString();
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.tax_payer_id);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return !etTaxPayerId.getText().toString().isEmpty();
    }

    public static CompensationFragmentTaxPayerId newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentTaxPayerId fragment = new CompensationFragmentTaxPayerId();
        fragment.setArguments(args);
        return fragment;
    }
}
