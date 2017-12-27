package com.getbewarned.connectinterpreter.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.presenters.CallPresenter;
import com.getbewarned.connectinterpreter.presenters.MainPresenter;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int RC_VIDEO_APP_PERM = 387;

    private ImageButton callBtn;
    private TextView minutesLeft;
    private TextView callToAction;
    private TextView userName;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callBtn = findViewById(R.id.call_button);
        minutesLeft = findViewById(R.id.left_value_label);
        callToAction = findViewById(R.id.call_to_action);
        userName = findViewById(R.id.user_name);

        presenter = new MainPresenter(this);


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartCallPressed();
            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.userNamePressed();
            }
        });

        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    public void showLeftTime(String leftTime) {
        minutesLeft.setText(leftTime);
    }

    @Override
    public void toggleCallAvailability(boolean available) {
        callBtn.setEnabled(available);
        if (available) {
            callBtn.setBackgroundResource(R.drawable.call_button_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                callBtn.setImageDrawable(getDrawable(R.drawable.ic_telephone));
            } else {
                callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_telephone));
            }
            callToAction.setText(R.string.main_call_to_action);
        } else {
            callBtn.setBackgroundResource(R.drawable.no_call_button_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                callBtn.setImageDrawable(getDrawable(R.drawable.ic_hourglass));
            } else {
                callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass));
            }
            callToAction.setText(R.string.no_minutes_left);
        }
    }

    @Override
    public void showChecking() {
        callBtn.setEnabled(false);
        callBtn.setBackgroundResource(R.drawable.no_call_button_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            callBtn.setImageDrawable(getDrawable(R.drawable.ic_hourglass));
        } else {
            callBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass));
        }
        callToAction.setText(R.string.main_wait_minutes_loading);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String message) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    public void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            presenter.onPermissionsGranted();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_rationale_call), RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void askForName(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_name, null);
        final EditText nameInput = view.findViewById(R.id.name_input);
        AlertDialog nameDialog = builder.setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameInput.getText().toString();
                        if (name.isEmpty()) {
                            return;
                        }
                        presenter.nameChanged(name);
                        dialog.dismiss();
                    }
                }).create();
        nameInput.setText(name);
        nameDialog.show();
    }

    @Override
    public void updateUserName(String name) {
        userName.setText(getString(R.string.user_name, name));
    }


    @Override
    public void askForReason() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.reason_alert_title));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(getResources().getStringArray(R.array.call_reasons));

        builderSingle.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = arrayAdapter.getItem(which);
                presenter.reasonSelected(reason);
                dialog.dismiss();
            }
        });

        builderSingle.show();

    }

    @Override
    public void navigateToCallFor(String reason) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(CallPresenter.REASON_EXTRA, reason);
        startActivity(intent);
    }

}
