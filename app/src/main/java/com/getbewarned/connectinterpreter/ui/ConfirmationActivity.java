package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.ConfirmationView;
import com.getbewarned.connectinterpreter.presenters.ConfirmationPresenter;

import javax.annotation.Nullable;

public class ConfirmationActivity extends NoStatusBarActivity implements ConfirmationView {

    private EditText codeField;
    private Button loginBtn;

    private TextView wrongNumber;
    private TextView needHelp;
    private TextView confirmationDesc;

    private ConfirmationPresenter presenter;

    TextView.OnEditorActionListener imeDoneListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginBtn.performClick();
                UiUtils.hideKeyboard(getCurrentFocus());
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_v2);

        codeField = findViewById(R.id.code);
        loginBtn = findViewById(R.id.login_button);
        confirmationDesc = findViewById(R.id.confirmation_description);
        wrongNumber = findViewById(R.id.wrong_number);
        wrongNumber.setPaintFlags(wrongNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        needHelp = findViewById(R.id.need_help);
        needHelp.setPaintFlags(needHelp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loginPressed(codeField.getText().toString());
            }
        });

        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.wrongNumberPressed();
            }
        });

        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.requestLoginHelp();
            }
        });

        presenter = new ConfirmationPresenter(this);
        presenter.onCreate(getIntent().getExtras());

        codeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    codeField.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                }
                else {
                    codeField.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginBtn.setActivated(!s.toString().isEmpty());
            }
        });
        codeField.setOnEditorActionListener(imeDoneListener);
        Analytics.getInstance().trackEvent(Events.EVENT_CODE_OPENED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void navigateToApp() {
        Intent intent = new Intent(this, NewMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void toggleEnabledRequestBtn(boolean enabled) {
//        getCodeBtn.setEnabled(enabled);
    }

    @Override
    public void toggleEnabledLoginBtn(boolean enabled) {
        loginBtn.setEnabled(enabled);
    }

    @Override
    public void setCode(String code) {
        codeField.setText(code);
    }

    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void showNumber(String phone) {
        String message = getString(R.string.confirmation_desc, phone);
        confirmationDesc.setText(message);
    }

    @Override
    public void showHelpRequested() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_requested_title)
                .setMessage(R.string.help_requested_text)
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
    public void showError(String message, @Nullable final Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_global)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigateBack();
                    }
                });
        if (throwable != null) {
            builder.setNeutralButton(R.string.send_error, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    StringBuilder bodyStringBuilder = new StringBuilder();
                    bodyStringBuilder.append(throwable.getMessage());
                    for (StackTraceElement e : throwable.getStackTrace()) {
                        bodyStringBuilder.append("\n");
                        bodyStringBuilder.append(e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + ":" + e.getLineNumber() + ")");
                    }

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"developers@getbewarned.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Ошибка");
                    intent.putExtra(Intent.EXTRA_TEXT, bodyStringBuilder.toString());

                    startActivity(Intent.createChooser(intent, "Отправить ошибку"));
                }
            });
        }
        builder.create()
                .show();
    }

    @Override
    public void navigateInputName() {
        Intent intent = new Intent(this, NameInputActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
