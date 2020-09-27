package com.getbewarned.connectinterpreter.ui.compensation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.CompensationPageAdapter;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

public class CompensationActivity extends NoStatusBarActivity implements CompensationDataConsumer {
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
    CompensationPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compensation);

        // toolbar
        toolbarTitle = findViewById(R.id.tv_toolbar_title);

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
        adapter = new CompensationPageAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("page onPageScrolled", String.valueOf(viewPager.getCurrentItem()));
                ((CompensationStep) currentFragment()).initData();
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("page onPageSelected", String.valueOf(viewPager.getCurrentItem()));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    Log.e("page onPageScrollStateC", String.valueOf(viewPager.getCurrentItem()));
                }
            }
        });
        viewPager.setAdapter(adapter);


        //back and continue
        bContinue = findViewById(R.id.b_continue);
        flBack = findViewById(R.id.fl_back);

        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CompensationStep) currentFragment()).storeData();
            }
        });
    }

    @Override
    public void update(String title, String nextButtonText, Boolean isNextButtonActive) {
        toolbarTitle.setText(title);
        bContinue.setText(nextButtonText);
        bContinue.setActivated(isNextButtonActive);

    }

    private Fragment currentFragment() {
        return (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
    }

}
