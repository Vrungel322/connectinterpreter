package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.HumanTime;

/**
 * income:
 * [MILLIS_KEY] - Long - def. value = 5 * 60 * 1000L - millisecons for count down timer
 * <p>
 * NOTE:
 * If user tap on "Cancel" button - Activity.RESULT_OK will be send.
 */
public class WaitCallResponseActivity extends NoStatusBarActivity {
    static final int RC = 999;
    static final String MILLIS_KEY_LONG = "159";

    static Activity activity = null;

    private Long leftTime;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_call_response);
        activity = this;
        leftTime = getIntent().getLongExtra(MILLIS_KEY_LONG, 5 * 60 * 1000L); // min * sec * millis

        animatePhoneIcon();

        (findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        final TextView timer = ((TextView) findViewById(R.id.tv_count_down_timer_to_connect));

        countDownTimer = new CountDownTimer(leftTime, 1000) {

            public void onTick(long millisUntilFinished) {
                String formattedTime = new HumanTime(WaitCallResponseActivity.this, millisUntilFinished).getTime();
                timer.setText(formattedTime);
            }

            public void onFinish() {
                timer.setText(NewMainActivity.ZERO_TIME);
                countDownTimer = null;
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        activity = null;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();
    }

    private void animatePhoneIcon() {
        ImageView big = findViewById(R.id.iv_circle_big);
        ImageView small = findViewById(R.id.iv_circle_small);

        Animation rotationBig = AnimationUtils.loadAnimation(WaitCallResponseActivity.this, R.anim.rotate_big);
        rotationBig.setFillAfter(true);
        rotationBig.setInterpolator(new LinearInterpolator());
        big.startAnimation(rotationBig);

        Animation rotationSmall = AnimationUtils.loadAnimation(WaitCallResponseActivity.this, R.anim.rotate_small);
        rotationSmall.setFillAfter(true);
        rotationSmall.setInterpolator(new LinearInterpolator());
        small.startAnimation(rotationSmall);
    }
}
