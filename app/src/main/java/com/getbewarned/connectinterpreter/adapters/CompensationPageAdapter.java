package com.getbewarned.connectinterpreter.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentDateOfBirth;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentFinal;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentFullName;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentInsuranceId;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentPassportData;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentRegistrationAddress;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentTaxPayerId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompensationPageAdapter extends FragmentStatePagerAdapter {

    private static List<Fragment> fragments;

    public CompensationPageAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList(Arrays.asList(
                CompensationFragmentFullName.newInstance(),
                CompensationFragmentDateOfBirth.newInstance(),
                CompensationFragmentPassportData.newInstance(),
                CompensationFragmentTaxPayerId.newInstance(),
                CompensationFragmentInsuranceId.newInstance(),
                CompensationFragmentRegistrationAddress.newInstance(),
                CompensationFragmentFinal.newInstance()
        ));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
