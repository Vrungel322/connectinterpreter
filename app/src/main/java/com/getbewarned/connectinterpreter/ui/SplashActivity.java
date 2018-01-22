package com.getbewarned.connectinterpreter.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    @Override
    protected void onResume() {
        super.onResume();
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            config.activateFetched();
                            checkVersion();
                        } else {
                            startApp();
                        }
                    }
                });
    }

    private void checkVersion() {
        int playStoreVersionCode = (int) FirebaseRemoteConfig.getInstance().getLong("android_actual_version_code");
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentAppVersionCode = pInfo.versionCode;
            if (playStoreVersionCode > currentAppVersionCode) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.app_outdated)
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

                builder.create().show();
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        startApp();
    }

    private void startApp() {
        UserManager userManager = new UserManager(this);
        Intent intent;
        if (userManager.getUserToken() == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
             intent = new Intent(this, MainActivity.class);

        }
        startActivity(intent);
        finish();
    }
}
