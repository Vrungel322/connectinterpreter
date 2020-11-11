package com.getbewarned.connectinterpreter.ui.compensation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.CompensationPageAdapter;
import com.getbewarned.connectinterpreter.custon_ui_elements.SwipableViewPager;
import com.getbewarned.connectinterpreter.interfaces.CompensationView;
import com.getbewarned.connectinterpreter.presenters.CompensationPresenter;
import com.getbewarned.connectinterpreter.ui.NoStatusBarActivity;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataConsumer;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationStep;

import java.util.ArrayList;
import java.util.List;


/**
 * out:
 * [COMPENSATION_DATA] - [CompensationDataHolder] - get as Serializable
 */
public class CompensationActivity extends NoStatusBarActivity implements CompensationDataConsumer, CompensationView {
    public static final int RC = 989;
    public static final String COMPENSATION_DATA = "COMPENSATION_DATA";

    TextView toolbarTitle;
    List<Pair<TextView, View>> indicators;
    SwipableViewPager viewPager;
    Button bContinue;
    FrameLayout flBack;
    CompensationPageAdapter adapter;
    CompensationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compensation);
        presenter = new CompensationPresenter(this);
        // toolbar
        toolbarTitle = findViewById(R.id.tv_toolbar_title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // step indicators
        indicators = new ArrayList<>();
        indicators.add(new Pair(findViewById(R.id.tv_indicator_1), null));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_2), findViewById(R.id.step_divider_1_2)));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_3), findViewById(R.id.step_divider_2_3)));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_4), findViewById(R.id.step_divider_3_4)));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_5), findViewById(R.id.step_divider_4_5)));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_6), findViewById(R.id.step_divider_5_6)));
        indicators.add(new Pair(findViewById(R.id.tv_indicator_7), findViewById(R.id.step_divider_6_7)));

        // view pager
        viewPager = findViewById(R.id.vp_steps);
        viewPager.setSwipeEnabled(false);
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
                        presenter.updateCompensationInfo();
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
        CompensationDataHolder.dispose();
        super.onDestroy();
    }

    @Override
    public void update(String title, String nextButtonText, Boolean isNextButtonActive) {
        if (((BaseCompensationStep) currentFragment()).getTitle().equals(title)) {
            toolbarTitle.setText(title);
            bContinue.setText(nextButtonText);
            bContinue.setActivated(isNextButtonActive);
            updateStepsIndicator();
        }
    }

    private Fragment currentFragment() {
        return (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
    }

    private void updateStepsIndicator() {
        int currentIndex = viewPager.getCurrentItem();
        for (int i = 0; i <= indicators.size() - 1; i++) {
            indicators.get(i).first.setActivated(false);
            indicators.get(i).first.setScaleX(1.f);
            indicators.get(i).first.setScaleY(1.f);
            if (indicators.get(i).second != null) indicators.get(i).second.setVisibility(View.GONE);
        }
        for (int i = 0; i <= currentIndex; i++) {
            indicators.get(i).first.setActivated(true);
            indicators.get(i).first.setScaleX(1.2f);
            indicators.get(i).first.setScaleY(1.2f);
            if (indicators.get(i).second != null)
                indicators.get(i).second.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message, @Nullable Throwable throwable) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_global)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void goBack() {
        finish();
    }
}
