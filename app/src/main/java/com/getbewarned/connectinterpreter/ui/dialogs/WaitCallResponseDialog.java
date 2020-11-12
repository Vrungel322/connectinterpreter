package com.getbewarned.connectinterpreter.ui.dialogs;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.models.HumanTime;
import com.getbewarned.connectinterpreter.ui.NewMainActivity;

public class WaitCallResponseDialog extends NoBackgroundDialog {
    public static final String TAG = "WaitCallResponseDialog";
    private Long leftTime = 5 * 60 * 1000L;// min * sec * millis
    private WaitCallResponseListener listener;
    private CountDownTimer countDownTimer;

    public void setDialogData(Long leftTimeMillis, WaitCallResponseListener listener) {
        this.leftTime = leftTimeMillis;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_wait_for_call_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animatePhoneIcon(view);

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDeclineToWait();
                dismissAllowingStateLoss();
            }
        });

        final TextView timer = (TextView) view.findViewById(R.id.tv_count_down_timer_to_connect);

        countDownTimer = new CountDownTimer(leftTime, 1000) {
            public void onTick(long millisUntilFinished) {
                String formattedTime = new HumanTime(getContext(), millisUntilFinished).getTime();
                timer.setText(formattedTime);
            }

            public void onFinish() {
                timer.setText(NewMainActivity.ZERO_TIME);
                countDownTimer = null;
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroyView();
    }

    private void animatePhoneIcon(View view) {
        ImageView big = view.findViewById(R.id.iv_circle_big);
        ImageView small = view.findViewById(R.id.iv_circle_small);

        Animation rotationBig = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_big);
        rotationBig.setFillAfter(true);
        rotationBig.setInterpolator(new LinearInterpolator());
        big.startAnimation(rotationBig);

        Animation rotationSmall = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_small);
        rotationSmall.setFillAfter(true);
        rotationSmall.setInterpolator(new LinearInterpolator());
        small.startAnimation(rotationSmall);
    }
}
