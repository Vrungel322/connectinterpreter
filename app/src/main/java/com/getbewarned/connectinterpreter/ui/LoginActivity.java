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
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!acceptCheck.isChecked()) {
                    return;
                }
                presenter.login(phoneField.getText().toString(), codeField.getText().toString());
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
}
