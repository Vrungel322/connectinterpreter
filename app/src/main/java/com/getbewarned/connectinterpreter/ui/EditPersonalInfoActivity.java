package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.EditPersonalInfoView;
import com.getbewarned.connectinterpreter.presenters.DataStored;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info_v2);

        // toolbar
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

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bSave.isActivated()) {
                    presenter.saveUserData(etName.getText().toString(),
                            etLastName.getText().toString(),
                            etPatronymicName.getText().toString(),
                            etCountry.getText().toString(),
                            etCity.getText().toString(),
                            new DataStored() {
                                @Override
                                public void dataStored() {
                                    finish();
                                }
                            });
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
}