package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.HumanTime;

public class WaitCallResponseActivity extends NoStatusBarActivity {
    static final int RC = 999;

    static Activity activity = null;

    private final Long leftTime = 5 * 60 * 1000L; // min * sec * millis

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_call_response);
        activity = this;

        (findViewById(R.id.tv_calncel)).setOnClickListener(new View.OnClickListener() {
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
        countDownTimer.cancel();
        countDownTimer = null;
        super.onDestroy();
    }
}
