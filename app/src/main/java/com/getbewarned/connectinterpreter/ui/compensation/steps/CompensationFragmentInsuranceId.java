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

public class CompensationFragmentInsuranceId extends BaseCompensationStep {
    EditText etInsuranceId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_insurance_id, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etInsuranceId = view.findViewById(R.id.et_insurance_id);

        etInsuranceId.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    public void initData() {
        etInsuranceId.setText(CompensationDataHolder.getInstance().insuranceId);
        super.initData();
    }

    @Override
    public void storeData() {
        CompensationDataHolder.getInstance().insuranceId = etInsuranceId.getText().toString();
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.insurance_id);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return !etInsuranceId.getText().toString().isEmpty();
    }

    public static CompensationFragmentInsuranceId newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentInsuranceId fragment = new CompensationFragmentInsuranceId();
        fragment.setArguments(args);
        return fragment;
    }

}
