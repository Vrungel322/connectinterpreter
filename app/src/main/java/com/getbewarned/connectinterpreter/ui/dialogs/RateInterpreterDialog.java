package com.getbewarned.connectinterpreter.ui.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getbewarned.connectinterpreter.R;

public class RateInterpreterDialog extends NoBackgroundDialog {

    private EditText feedback;
    private TextView send;
    private RateInterpreterListener onDoneListener;

    public void setListener(RateInterpreterListener onDone) {
        this.onDoneListener = onDone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_rate_interpreter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (view.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDoneListener != null) onDoneListener.rateSkipped();
                dismissAllowingStateLoss();
            }
        });

        feedback = view.findViewById(R.id.et_feedback);
        send = view.findViewById(R.id.tv_send_feedback);
        final RatingBar ratingBar = view.findViewById(R.id.rating);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ratingBar.getRating();
                String text = feedback.getText().toString();
                if (onDoneListener != null) onDoneListener.onRateDone(rating, text);
                dismissAllowingStateLoss();
            }
        });


        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    send.setVisibility(View.GONE);
                } else {
                    send.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}