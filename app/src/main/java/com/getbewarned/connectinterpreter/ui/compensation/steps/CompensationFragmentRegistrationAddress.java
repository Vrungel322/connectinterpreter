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

public class CompensationFragmentRegistrationAddress extends BaseCompensationStep {
    EditText etCity;
    EditText etStreet;
    EditText etBuildingNumber;
    EditText etApartmentNumber;
    EditText etPostId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_registration_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCity = view.findViewById(R.id.et_city);
        etStreet = view.findViewById(R.id.et_street);
        etBuildingNumber = view.findViewById(R.id.et_building_number);
        etApartmentNumber = view.findViewById(R.id.et_apartment_number);
        etPostId = view.findViewById(R.id.et_post_id);

        etCity.addTextChangedListener(defaultTextWatcher);
        etStreet.addTextChangedListener(defaultTextWatcher);
        etBuildingNumber.addTextChangedListener(defaultTextWatcher);
        etApartmentNumber.addTextChangedListener(defaultTextWatcher);
        etPostId.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    public void initData() {
        etCity.setText(CompensationDataHolder.getInstance().city);
        etStreet.setText(CompensationDataHolder.getInstance().streetAddress);
        etBuildingNumber.setText(CompensationDataHolder.getInstance().buildingNumber);
        etApartmentNumber.setText(CompensationDataHolder.getInstance().apartmentNumber);
        etPostId.setText(CompensationDataHolder.getInstance().postId);
        super.initData();
    }

    @Override
    public void storeData() {
        CompensationDataHolder.getInstance().city = etCity.getText().toString();
        CompensationDataHolder.getInstance().streetAddress = etStreet.getText().toString();
        CompensationDataHolder.getInstance().buildingNumber = etBuildingNumber.getText().toString();
        CompensationDataHolder.getInstance().apartmentNumber = etApartmentNumber.getText().toString();
        CompensationDataHolder.getInstance().postId = etPostId.getText().toString();
    }

    @Override
    public String getTitle() {
        return this.getString(R.string.registration_address);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return !etCity.getText().toString().isEmpty()
                && !etStreet.getText().toString().isEmpty()
                && !etBuildingNumber.getText().toString().isEmpty()
                && !etApartmentNumber.getText().toString().isEmpty()
                && !etPostId.getText().toString().isEmpty();
    }

    public static CompensationFragmentRegistrationAddress newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentRegistrationAddress fragment = new CompensationFragmentRegistrationAddress();
        fragment.setArguments(args);
        return fragment;
    }

}
