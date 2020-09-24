package com.getbewarned.connectinterpreter.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.SplashView;
import com.getbewarned.connectinterpreter.presenters.SplashPresenter;


public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this);
        presenter.onCreate(getIntent().getExtras());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void navigateToLogin() {
        // stub
//        Intent intent = new Intent(this, ConfirmationActivity.class);
//        intent.putExtra(ConfirmationPresenter.PHONE_EXTRA, "test");
        // stub 2
        Intent intent = new Intent(this, NameInputActivity.class);
        startActivityForResult(intent, NameInputActivity.RC);

        // real code
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void navigateToApp() {
        Intent intent = new Intent(this, NewMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showUpdateAlert(String version, boolean required) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message;
        if (required) {
            message = getString(R.string.update_required);
        } else {
            message = getString(R.string.update_recommended);
        }
        builder.setTitle(getString(R.string.new_app_version, version))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

        if (!required) {
            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    presenter.updateSkipped();
                }
            });
        }

        builder.create().show();
    }

    @Override
    public void showNewVersionInfo(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.whats_new)
                .setMessage(description)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.infoAccepted();
                    }
                });
        builder.create().show();
    }
}
