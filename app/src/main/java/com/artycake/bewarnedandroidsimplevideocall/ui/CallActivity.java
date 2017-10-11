package com.artycake.bewarnedandroidsimplevideocall.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.artycake.bewarnedandroidsimplevideocall.R;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.CallView;
import com.artycake.bewarnedandroidsimplevideocall.presenters.CallPresenter;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CallActivity extends AppCompatActivity implements CallView {

    private static final int RC_VIDEO_APP_PERM = 387;
    private FrameLayout selfContainer;
    private FrameLayout interpreterContainer;
    private ImageButton endCallButton;
    private TextView timeLeft;
    private TextView duration;

    private CallPresenter presenter;

    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        selfContainer =  findViewById(R.id.self_container);
        interpreterContainer = findViewById(R.id.interpreter_container);
        endCallButton =  findViewById(R.id.end_call_button);
        timeLeft =  findViewById(R.id.time_left);
        duration = findViewById(R.id.duration);

        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.endCall();
            }
        });

        presenter = new CallPresenter(this);
        presenter.onCreate(getIntent().getExtras());

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
    public void updateLeftTime(String leftTime) {
        this.timeLeft.setText(leftTime);
    }

    @Override
    public void updateCurrentCallDuration(String duration) {
        this.duration.setText(duration);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void toggleEndCallButtonVisibility(boolean visible) {
        if (visible) {
            endCallButton.setVisibility(View.VISIBLE);
        } else {
            endCallButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showIndicator() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCancelable(false);
            loadingDialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.endCall();
                            dialog.dismiss();
                        }
                    }
            );
            loadingDialog.setTitle(getString(R.string.video_call_waiting_title));
            loadingDialog.setMessage(getString(R.string.video_call_waiting_text));
            loadingDialog.show();
        }
    }

    @Override
    public void hideIndicator() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_call_failed)
                .setMessage(R.string.err_call_try_later)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigateBack();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void showSelfView(View view) {
        selfContainer.addView(view);
    }

    @Override
    public void showInterpreterView(View view) {
        interpreterContainer.addView(view);
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
