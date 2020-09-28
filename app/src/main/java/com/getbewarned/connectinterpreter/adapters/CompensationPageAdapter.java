package com.getbewarned.connectinterpreter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentDateOfBirth;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentFullName;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentPassportData;
import com.getbewarned.connectinterpreter.ui.compensation.steps.CompensationFragmentTaxPayerId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompensationPageAdapter extends FragmentStatePagerAdapter {

    private static final List<Fragment> fragments = new ArrayList(Arrays.asList(
            CompensationFragmentFullName.newInstance(),
            CompensationFragmentDateOfBirth.newInstance(),
            CompensationFragmentPassportData.newInstance(),
            CompensationFragmentTaxPayerId.newInstance()
    ));

    public CompensationPageAdapter(FragmentManager fm) {
        super(fm);
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
