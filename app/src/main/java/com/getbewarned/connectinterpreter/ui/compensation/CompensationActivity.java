package com.getbewarned.connectinterpreter.ui.compensation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.CompensationPageAdapter;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

import java.util.ArrayList;


/**
 * out:
 * [COMPENSATION_DATA] - [CompensationDataHolder] - get as Serializable
 */
public class CompensationActivity extends NoStatusBarActivity implements CompensationDataConsumer {
    public static final String COMPENSATION_DATA = "COMPENSATION_DATA";

    TextView toolbarTitle;
    ArrayList<FrameLayout> indicators;
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
        indicators = new ArrayList();
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_1));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_2));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_3));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_4));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_5));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_6));
        indicators.add((FrameLayout) findViewById(R.id.fl_indicator_7));

        // view pager
        viewPager = findViewById(R.id.vp_steps);
        adapter = new CompensationPageAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    ((CompensationStep) currentFragment()).initData();
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
                if (bContinue.isActivated()) {
                    ((CompensationStep) currentFragment()).storeData();

                    int nextIndex = viewPager.getCurrentItem() + 1;
                    if (nextIndex >= adapter.getCount()) {
                        Intent data = new Intent();
                        data.putExtra(COMPENSATION_DATA, CompensationDataHolder.getInstance());
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    } else {
                        viewPager.setCurrentItem(nextIndex);
                    }
                }
            }
        });

        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevIndex = viewPager.getCurrentItem() - 1;
                if (prevIndex < 0) {
                    onBackPressed();
                } else {
                    viewPager.setCurrentItem(prevIndex);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CompensationDataHolder.dispose();
    }

    @Override
    public void update(String title, String nextButtonText, Boolean isNextButtonActive) {
        toolbarTitle.setText(title);
        bContinue.setText(nextButtonText);
        bContinue.setActivated(isNextButtonActive);
        updateStepsIndicator();
    }

    private Fragment currentFragment() {
        return (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
    }

    private void updateStepsIndicator() {
        int currentIndex = viewPager.getCurrentItem();
        for (int i = 0; i <= indicators.size() - 1; i++) {
            indicators.get(i).setActivated(false);
        }
        for (int i = 0; i <= currentIndex; i++) {
            indicators.get(i).setActivated(true);
        }

    }
}
