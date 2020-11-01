package com.getbewarned.connectinterpreter.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;

/**
 * income:
 * [TEXT_KEY] - error message
 */
public class ErrorActivity extends NoStatusBarActivity {
     static final String TEXT_KEY = "307";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        String errorMsg = getIntent().getStringExtra(TEXT_KEY);
        ((TextView) findViewById(R.id.tv_error_message)).setText(errorMsg);

        (findViewById(R.id.tv_ok_error)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
