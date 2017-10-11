package com.artycake.bewarnedandroidsimplevideocall.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.artycake.bewarnedandroidsimplevideocall.R;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.MainView;
import com.artycake.bewarnedandroidsimplevideocall.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainView {

    private ImageButton callBtn;
    private TextView minutesLeft;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callBtn = (ImageButton) findViewById(R.id.call_button);
        minutesLeft = (TextView) findViewById(R.id.left_value_label);

        presenter = new MainPresenter(this);


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartCallPressed();
            }
        });

        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    public void showLeftTime(String leftTime) {
        minutesLeft.setText(leftTime);
    }

    @Override
    public void toggleCallButtonEnabled(boolean enabled) {
        callBtn.setEnabled(enabled);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void navigateToCall() {
        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
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
}
