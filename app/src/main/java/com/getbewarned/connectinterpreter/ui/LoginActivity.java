package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.getbewarned.connectinterpreter.NetworkLinks;
import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.LoginView;
import com.getbewarned.connectinterpreter.models.Country;
import com.getbewarned.connectinterpreter.presenters.ConfirmationPresenter;
import com.getbewarned.connectinterpreter.presenters.LoginPresenter;

public class LoginActivity extends NoStatusBarActivity implements LoginView {

    private static final int COUNTRY_REQUEST_CODE = 782;

    private EditText phoneField;
    private Button continueBtn;
    private CheckBox acceptCheck;
    private TextView acceptText;

    private LoginPresenter presenter;

    TextView.OnEditorActionListener imeDoneListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                continueBtn.performClick();
                UiUtils.hideKeyboard(getCurrentFocus());
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);
        phoneField = findViewById(R.id.phone);
        continueBtn = findViewById(R.id.get_code_button);
        acceptCheck = findViewById(R.id.accept_check);
        acceptText = findViewById(R.id.accept_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept), Html.FROM_HTML_MODE_COMPACT));
        } else {
            acceptText.setText(Html.fromHtml(getString(R.string.login_accept)));
        }
        UiUtils.clickSubText(acceptText, getString(R.string.login_tos), new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkLinks.TOS)));
            }
        });

        UiUtils.clickSubText(acceptText, getString(R.string.login_privacy_policy), new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkLinks.PRIVACY_POLICY)));
            }
        });

        presenter = new LoginPresenter(this);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (continueBtn.isActivated()) {
                    String phone = "+" + phoneField.getText().toString();
//                    String phone = "+380661805980";
//                    String phone = "+380689647569";
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
                toggleActiveContinueBtn();
            }
        });
        phoneField.setOnEditorActionListener(imeDoneListener);

        acceptCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleActiveContinueBtn();
            }
        });
        Analytics.getInstance().trackEvent(Events.EVENT_LOGIN_OPENED);
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

    private void toggleActiveContinueBtn() {
        continueBtn.setActivated(!phoneField.getText().toString().equals("") && acceptCheck.isChecked());

    }
}
