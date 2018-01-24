package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.LoginView;
import com.getbewarned.connectinterpreter.presenters.ConfirmationPresenter;
import com.getbewarned.connectinterpreter.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText countryCodeField;
    private EditText phoneField;
    private Button continueBtn;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        countryCodeField = findViewById(R.id.country_code);
        phoneField = findViewById(R.id.phone);
        continueBtn = findViewById(R.id.get_code_button);


        presenter = new LoginPresenter(this);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = countryCodeField.getText().toString() + phoneField.getText().toString();
                presenter.continuePressed(phone);
            }
        });
    }

    @Override
    public void toggleEnabledLoginBtn(boolean enabled) {
        continueBtn.setEnabled(enabled);
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
    public void navigateToConfirmation(String phone) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationPresenter.PHONE_EXTRA, phone);
        startActivity(intent);
    }
}
