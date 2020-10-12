package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.NameInputView;
import com.getbewarned.connectinterpreter.presenters.NameInputPresenter;

/**
 * out:
 * [NAME_KEY] - [String] - entered name
 * <p>
 * rc: [NameInputActivity.RC]
 */
public class NameInputActivity extends NoStatusBarActivity implements NameInputView {
    public static final int RC = 774;
    public static final String NAME_KEY = "NAME_KEY";

    private EditText etName;
    private Button bContinue;
    private NameInputPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input_v2);
        presenter = new NameInputPresenter(this);
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
                    presenter.continueClicked(etName.getText().toString());
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return getApplication();
    }

    @Override
    public void onBackPressed() {
        navigateToApp();
    }

    @Override
    public void showError(String message, @javax.annotation.Nullable final Throwable throwable) {
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
    public void navigateToApp() {
        Intent intent = new Intent(this, NewMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
