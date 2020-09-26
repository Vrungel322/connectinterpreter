package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.ProfileView;
import com.getbewarned.connectinterpreter.presenters.ProfilePresenterV2;

public class ProfileActivityV2 extends NoStatusBarActivity implements ProfileView {

    private ProfilePresenterV2 presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_v2);

        // toolbar
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.drawer_profile);
        ((ImageView) findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((ImageView) findViewById(R.id.iv_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.logout();
            }
        });
        ((ImageView) findViewById(R.id.iv_edit_personal_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivityV2.this, EditPersonalInfoActivity.class);
                startActivity(intent);
            }
        });
        ((FrameLayout) findViewById(R.id.fl_buy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivityV2.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });

        presenter = new ProfilePresenterV2(this, this);
        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void updateUserData(String userName, String userLastName, String userPatronymic, String userCountry, String userCity, String userPhone) {
        ((TextView) findViewById(R.id.tv_user_name)).setText(userLastName + " " + userName + " " + userPatronymic);
        if (!userCity.isEmpty() && !userCountry.isEmpty()) {
            ((TextView) findViewById(R.id.tv_country_city)).setText(userCountry + "," + userCity);
        }
        ((TextView) findViewById(R.id.tv_phone_number)).setText(userPhone);
    }

    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
