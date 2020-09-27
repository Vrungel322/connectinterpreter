package com.getbewarned.connectinterpreter.ui.compensation;

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
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

public class CompensationFragmentFullName extends BaseCompensationStep {
    EditText etLastName;
    EditText etName;
    EditText etPatronymic;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateParent();
        }
    };

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

        etLastName.addTextChangedListener(textWatcher);
        etName.addTextChangedListener(textWatcher);
        etPatronymic.addTextChangedListener(textWatcher);

    }

    @Override
    public void initData() {
        etLastName.setText(CompensationDataHolder.dataHolder.lastName);
        etName.setText(CompensationDataHolder.dataHolder.firstName);
        etPatronymic.setText(CompensationDataHolder.dataHolder.patronymic);
        updateParent();
    }


    @Override
    public void storeData() {
        CompensationDataHolder.dataHolder.lastName = etLastName.getText().toString();
        CompensationDataHolder.dataHolder.firstName = etName.getText().toString();
        CompensationDataHolder.dataHolder.patronymic = etPatronymic.getText().toString();
    }

    @Override
    String getTitle() {
        return this.getString(R.string.full_name);
    }

    @Override
    String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    Boolean getIsNextButtonActive() {
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
