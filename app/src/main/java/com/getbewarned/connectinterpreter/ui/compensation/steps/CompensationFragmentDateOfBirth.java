package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;

public class CompensationFragmentDateOfBirth extends BaseCompensationStep {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_date_of_birth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public String getTitle() {
        return this.getString(R.string.date_of_birth);
    }

    @Override
    public String getNextButtonText() {
        return this.getString(R.string.action_continue);
    }

    @Override
    public Boolean getIsNextButtonActive() {
        return true;
    }

    @Override
    public void initData() {
        Log.d("qq","qq");
        super.initData();
    }

    @Override
    public void storeData() {

    }

    public static CompensationFragmentDateOfBirth newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentDateOfBirth fragment = new CompensationFragmentDateOfBirth();
        fragment.setArguments(args);
        return fragment;
    }
}
