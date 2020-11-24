package com.getbewarned.connectinterpreter.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.UiUtils;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.EditPersonalInfoView;
import com.getbewarned.connectinterpreter.presenters.EditPersonalInfoPresenterV2;

public class EditPersonalInfoActivity extends NoStatusBarActivity implements EditPersonalInfoView {

    private EditPersonalInfoPresenterV2 presenter;
    private TextView tvSaveLater;
    private TextView tvPhone;
    private EditText etName;
    private EditText etLastName;
    private EditText etPatronymicName;
    private EditText etCountry;
    private EditText etCity;
    private Button bSave;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etName.getText().toString().isEmpty()
                    && etLastName.getText().toString().isEmpty()
                    && etPatronymicName.getText().toString().isEmpty()
                    && etCountry.getText().toString().isEmpty()
                    && etCity.getText().toString().isEmpty()) {
                bSave.setActivated(false);
            } else {
                bSave.setActivated(true);
            }
        }
    };

    TextView.OnEditorActionListener imeDoneListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                UiUtils.hideKeyboard(getCurrentFocus());
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info_v2);

        // toolbar
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.drawer_profile);
        ((ImageView) findViewById(R.id.iv_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.logout();
            }
        });

        // content
        tvPhone = findViewById(R.id.tv_phone_number);
        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etPatronymicName = findViewById(R.id.et_patronymic_name);
        etCountry = findViewById(R.id.et_country);
        etCity = findViewById(R.id.et_city);
        bSave = findViewById(R.id.b_save);
        tvSaveLater = findViewById(R.id.tv_save_later);
        tvSaveLater.setPaintFlags(tvSaveLater.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        etName.setOnEditorActionListener(imeDoneListener);
        etLastName.setOnEditorActionListener(imeDoneListener);
        etPatronymicName.setOnEditorActionListener(imeDoneListener);
        etCountry.setOnEditorActionListener(imeDoneListener);
        etCity.setOnEditorActionListener(imeDoneListener);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bSave.isActivated()) {
                    presenter.saveUserData(etName.getText().toString(),
                            etLastName.getText().toString(),
                            etPatronymicName.getText().toString(),
                            etCountry.getText().toString(),
                            etCity.getText().toString());
                }
            }
        });


        presenter = new EditPersonalInfoPresenterV2(this, this);
        presenter.onCreate(getIntent().getExtras());

        tvSaveLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Analytics.getInstance().trackEvent(Events.EVENT_EDIT_PROFILE_OPENED);
    }

    @Override
    public void showUsedData(String userPhone, String userName, String userLastName, String userPatronymic, String userCountry, String userCity) {
        tvPhone.setText(userPhone);
        etName.setText(userName);
        etLastName.setText(userLastName);
        etPatronymicName.setText(userPatronymic);
        etCountry.setText(userCountry);
        etCity.setText(userCity);

        etName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etPatronymicName.addTextChangedListener(textWatcher);
        etCountry.addTextChangedListener(textWatcher);
        etCity.addTextChangedListener(textWatcher);
    }

    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
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
    public void goBack() {
        finish();
    }
}
