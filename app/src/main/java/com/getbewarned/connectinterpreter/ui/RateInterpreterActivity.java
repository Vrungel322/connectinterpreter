package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;


public class RateInterpreterActivity extends NoStatusBarActivity {
    static final int RC = 175;
    static final String STARS_KEY = "605";
    static final String FEEDBACK_KEY = "606";

    EditText feedback;
    TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_interpreter);

        (findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        feedback = findViewById(R.id.et_feedback);
        send = findViewById(R.id.tv_send_feedback);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ((RatingBar) findViewById(R.id.rating)).getRating();
                String text = feedback.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(STARS_KEY, rating);
                intent.putExtra(FEEDBACK_KEY, text);
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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

    @Override
    public void onBackPressed() {

    }
}
