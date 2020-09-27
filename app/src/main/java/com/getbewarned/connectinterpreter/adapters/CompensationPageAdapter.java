package com.getbewarned.connectinterpreter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.getbewarned.connectinterpreter.ui.compensation.CompensationFragmentFullName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompensationPageAdapter extends FragmentStatePagerAdapter {

    private static final List<Fragment> fragments = new ArrayList(Collections.singletonList(
            CompensationFragmentFullName.newInstance()));

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
