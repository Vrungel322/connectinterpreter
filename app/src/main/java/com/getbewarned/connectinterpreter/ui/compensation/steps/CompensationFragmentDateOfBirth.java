package com.getbewarned.connectinterpreter.ui.compensation.steps;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.ui.compensation.BaseCompensationStep;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

import java.util.Calendar;

public class CompensationFragmentDateOfBirth extends BaseCompensationStep {

    DatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compensation_fragent_date_of_birth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.dp_date_of_birth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    storeData();
                }
            });
        }

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
        Long millis;
        if (CompensationDataHolder.getInstance().dateOfBirthMillis == null) {
            millis = System.currentTimeMillis();
        } else {
            millis = CompensationDataHolder.getInstance().dateOfBirthMillis;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        super.initData();
    }

    @Override
    public void storeData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.YEAR, datePicker.getYear());
        CompensationDataHolder.getInstance().dateOfBirthMillis = cal.getTimeInMillis();

    }

    public static CompensationFragmentDateOfBirth newInstance() {
        Bundle args = new Bundle();
        CompensationFragmentDateOfBirth fragment = new CompensationFragmentDateOfBirth();
        fragment.setArguments(args);
        return fragment;
    }
}
