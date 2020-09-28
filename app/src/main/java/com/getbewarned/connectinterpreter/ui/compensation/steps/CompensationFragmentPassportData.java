package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

public class CompensationFragmentPassportData extends BaseCompensationStep {
    EditText etPassportSerialCode;
    EditText etPassportNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_passport_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPassportSerialCode = view.findViewById(R.id.et_passport_serial_code);
        etPassportNumber = view.findViewById(R.id.et_passport_serial_number);

        etPassportSerialCode.addTextChangedListener(defaultTextWatcher);
        etPassportNumber.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    public void initData() {
        etPassportSerialCode.setText(CompensationDataHolder.getInstance().passportSerialCode);
        etPassportNumber.setText(CompensationDataHolder.getInstance().passportSerialNumber);
        super.initData();
    }

    @Override
    public void storeData() {
        CompensationDataHolder.getInstance().passportSerialCode = etPassportSerialCode.getText().toString();
        CompensationDataHolder.getInstance().passportSerialNumber = etPassportNumber.getText().toString();
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.passport);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return !etPassportSerialCode.getText().toString().isEmpty()
                && !etPassportNumber.getText().toString().isEmpty();
    }

    public static CompensationFragmentPassportData newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentPassportData fragment = new CompensationFragmentPassportData();
        fragment.setArguments(args);
        return fragment;
    }

}
