package com.getbewarned.connectinterpreter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getbewarned.connectinterpreter.R;

/**
 * out:
 * [NAME_KEY] - [String] - entered name
 *
 * rc: [NameInputActivity.RC]
 */
public class NameInputActivity extends AppCompatActivity {
    public static final int RC = 774;
    public static final String NAME_KEY = "NAME_KEY";

    private EditText etName;
    private Button bContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input_v2);
        etName = findViewById(R.id.et_name);
        bContinue = findViewById(R.id.b_continue);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bContinue.setActivated(!s.toString().equals(""));
            }
        });

        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bContinue.isActivated()) {
                    Intent data = new Intent().putExtra(NAME_KEY, etName.getText().toString());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}
