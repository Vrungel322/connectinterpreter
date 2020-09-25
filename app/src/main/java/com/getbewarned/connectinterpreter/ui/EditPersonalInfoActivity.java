package com.getbewarned.connectinterpreter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.EditPersonalInfoView;
import com.getbewarned.connectinterpreter.presenters.EditPersonalInfoPresenterV2;

public class EditPersonalInfoActivity extends NoStatusBarActivity implements EditPersonalInfoView {

    private EditPersonalInfoPresenterV2 presenter;

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

//        saveLater.setPaintFlags(saveLater.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        presenter = new EditPersonalInfoPresenterV2(this, this);
        presenter.onCreate(getIntent().getExtras());
    }

    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
