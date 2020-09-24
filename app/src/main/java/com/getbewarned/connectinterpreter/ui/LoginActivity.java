package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.LoginView;
import com.getbewarned.connectinterpreter.models.Country;
import com.getbewarned.connectinterpreter.presenters.ConfirmationPresenter;
import com.getbewarned.connectinterpreter.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final int COUNTRY_REQUEST_CODE = 782;

    //    private EditText countryCodeField;
    private EditText phoneField;
    private Button continueBtn;
    private CheckBox acceptCheck;
    private TextView acceptText;
//    private EditText countryField;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);
//        countryCodeField = findViewById(R.id.country_code);
        phoneField = findViewById(R.id.phone);
        continueBtn = findViewById(R.id.get_code_button);
        acceptCheck = findViewById(R.id.accept_check);
        acceptText = findViewById(R.id.accept_text);
//        countryField = findViewById(R.id.country);
//        countryField.setKeyListener(null);
//        countryField.setFocusable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept), Html.FROM_HTML_MODE_LEGACY));
        } else {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept)));
        }
        acceptText.setClickable(true);
        acceptText.setMovementMethod(LinkMovementMethod.getInstance());

//        countryField.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, CountryActivity.class);
//                startActivityForResult(intent, COUNTRY_REQUEST_CODE);
//            }
//        });


        presenter = new LoginPresenter(this);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (continueBtn.isActivated()) {
                    String phone = "+7" + phoneField.getText().toString();
                    presenter.continuePressed(phone, acceptCheck.isChecked());
                }
            }
        });

        phoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                continueBtn.setActivated(!s.toString().equals(""));
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
        if (isFinishing()) {
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToConfirmation(String phone) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationPresenter.PHONE_EXTRA, phone);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COUNTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            presenter.countrySelected(new Country(data.getStringExtra("name"), data.getStringExtra("code")));
        }
    }

    @Override
    public void setCountry(Country country) {
//        countryField.setText(country.getName());
//        countryCodeField.setText(country.getCode());
    }

    @Override
    public void confirmPhone(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_LoaderDialog);
        builder.setTitle(R.string.confirm_phone)
                .setMessage(phone)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.confirmedPressed();
                    }
                })
                .setNegativeButton(R.string.change_number, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
