package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.LoginView;
import com.getbewarned.connectinterpreter.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText phoneField;
    private EditText codeField;
    private TextInputLayout passwordContainer;
    private Button getCodeBtn;
    private Button loginBtn;
    private TextView hintTextView;
    private CheckBox acceptCheck;
    private TextView acceptText;
    private View accept;
    private TextInputLayout phoneLayout;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneField = findViewById(R.id.phone);
        codeField = findViewById(R.id.code);
        passwordContainer = findViewById(R.id.password_input_container);
        getCodeBtn = findViewById(R.id.get_code_button);
        loginBtn = findViewById(R.id.login_button);
        hintTextView = findViewById(R.id.login_hint);
        acceptCheck = findViewById(R.id.accept_check);
        acceptText = findViewById(R.id.accept_text);
        accept = findViewById(R.id.accept);
        phoneLayout = findViewById(R.id.phone_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept), Html.FROM_HTML_MODE_LEGACY));
        } else {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept)));
        }
        acceptText.setClickable(true);
        acceptText.setMovementMethod(LinkMovementMethod.getInstance());


        presenter = new LoginPresenter(this);

        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getCode(phoneField.getText().toString());
                getCodeBtn.setText(R.string.action_request_code);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login(phoneField.getText().toString(), codeField.getText().toString(), acceptCheck.isChecked());
            }
        });

        phoneField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    phoneLayout.setHint(getString(R.string.prompt_phone));
                    if (phoneField.getText().toString().isEmpty()) {
                        phoneField.setText("+");
                    }
                } else {
                    phoneLayout.setHint(getString(R.string.phone_example));
                    if (phoneField.getText().toString().equals("+")) {
                        phoneField.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void showPasswordAndLoginBtn() {
        passwordContainer.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
        accept.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateHint(String message) {
        hintTextView.setText(message);
    }

    @Override
    public void toggleEnabledRequestBtn(boolean enabled) {
        getCodeBtn.setEnabled(enabled);
    }

    @Override
    public void toggleEnabledLoginBtn(boolean enabled) {
        getCodeBtn.setEnabled(enabled);
    }

    @Override
    public void setCode(String code) {
        codeField.setText(code);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void navigateToApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
