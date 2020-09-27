package com.getbewarned.connectinterpreter.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;

public class CompensationActivity extends NoStatusBarActivity {
    TextView toolbarTitle;
    FrameLayout indicator1;
    FrameLayout indicator2;
    FrameLayout indicator3;
    FrameLayout indicator4;
    FrameLayout indicator5;
    FrameLayout indicator6;
    FrameLayout indicator7;
    ViewPager viewPager;
    Button bContinue;
    FrameLayout flBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compensation);

        // toolbar
        toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText(R.string.tariff_choose);

        // step indicators
        indicator1 = findViewById(R.id.fl_indicator_1);
        indicator2 = findViewById(R.id.fl_indicator_2);
        indicator3 = findViewById(R.id.fl_indicator_3);
        indicator4 = findViewById(R.id.fl_indicator_4);
        indicator5 = findViewById(R.id.fl_indicator_5);
        indicator6 = findViewById(R.id.fl_indicator_6);
        indicator7 = findViewById(R.id.fl_indicator_7);

        // view pager
        viewPager = findViewById(R.id.vp_steps);

        //back and continue
        bContinue = findViewById(R.id.b_continue);
        flBack = findViewById(R.id.fl_back);
    }
}
