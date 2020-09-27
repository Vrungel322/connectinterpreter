package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

public class CompensationFragmentFullName extends BaseCompensationStep {
    EditText etLastName;
    EditText etName;
    EditText etPatronymic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_full_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etLastName = view.findViewById(R.id.et_last_name);
        etName = view.findViewById(R.id.et_name);
        etPatronymic = view.findViewById(R.id.et_patronymic_name);

        etLastName.addTextChangedListener(defaultTextWatcher);
        etName.addTextChangedListener(defaultTextWatcher);
        etPatronymic.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // this needs only for first fragment - all other fragments will be updated via  viewPage OnPageChangeListener
        initData();
    }

    @Override
    public void initData() {
        etLastName.setText(CompensationDataHolder.getInstance().lastName);
        etName.setText(CompensationDataHolder.getInstance().firstName);
        etPatronymic.setText(CompensationDataHolder.getInstance().patronymic);
        super.initData();
    }


    @Override
    public void storeData() {
        CompensationDataHolder.getInstance().lastName = etLastName.getText().toString();
        CompensationDataHolder.getInstance().firstName = etName.getText().toString();
        CompensationDataHolder.getInstance().patronymic = etPatronymic.getText().toString();
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.full_name);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return !etLastName.getText().toString().isEmpty()
                && !etName.getText().toString().isEmpty()
                && !etPatronymic.getText().toString().isEmpty();
    }

    public static CompensationFragmentFullName newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentFullName fragment = new CompensationFragmentFullName();
        fragment.setArguments(args);
        return fragment;
    }
}
