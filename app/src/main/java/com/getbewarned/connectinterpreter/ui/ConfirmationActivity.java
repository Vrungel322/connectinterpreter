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
import com.getbewarned.connectinterpreter.interfaces.ConfirmationView;
import com.getbewarned.connectinterpreter.presenters.ConfirmationPresenter;

public class ConfirmationActivity extends AppCompatActivity implements ConfirmationView {

    private TextInputLayout passwordContainer;
    private EditText codeField;
    private Button loginBtn;
    private CheckBox acceptCheck;
    private TextView acceptText;
    private TextView wrongNumber;
    //    private Button getCodeBtn;
    private TextView confirmationDesc;

    private ConfirmationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        codeField = findViewById(R.id.code);
        passwordContainer = findViewById(R.id.password_input_container);
        loginBtn = findViewById(R.id.login_button);
        acceptCheck = findViewById(R.id.accept_check);
        acceptText = findViewById(R.id.accept_text);
        confirmationDesc = findViewById(R.id.confirmation_description);
        wrongNumber = findViewById(R.id.wrong_number);
//        getCodeBtn = findViewById(R.id.get_code_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept), Html.FROM_HTML_MODE_LEGACY));
        } else {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept)));
        }
        acceptText.setClickable(true);
        acceptText.setMovementMethod(LinkMovementMethod.getInstance());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loginPressed(codeField.getText().toString(), acceptCheck.isChecked());
            }
        });

        wrongNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.wrongNumberPressed();
            }
        });

        presenter = new ConfirmationPresenter(this);
        presenter.onCreate(getIntent().getExtras());
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
}
