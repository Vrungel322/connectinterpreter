package com.getbewarned.connectinterpreter.presenters;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.interfaces.AppVersionReceived;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.SplashView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.AppVersionResponse;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by artycake on 1/24/18.
 */

public class SplashPresenter implements Presenter {

    private SplashView view;
    private NetworkManager networkManager;
    private UserManager userManager;

    public SplashPresenter(SplashView view) {
        this.view = view;
        networkManager = new NetworkManager(view.getContext());
        userManager = new UserManager(view.getContext());
    }

    @Override
    public void onCreate(Bundle extras) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        checkVersion();
    }

    @Override
    public void onDestroy() {

    }

    private void checkVersion() {
        PackageInfo pInfo;
        try {
            pInfo = view.getContext().getPackageManager().getPackageInfo(view.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            navigateNext();
            return;
        }
        final int currentAppVersionCode = pInfo.versionCode;
        networkManager.getAppVersion(new AppVersionReceived() {
            @Override
            public void onAppVersionReceived(AppVersionResponse response) {
                if (currentAppVersionCode < response.getBuildNumber()) {
                    view.showUpdateAlert(response.getVersion(), response.isUpdateRequired());
                } else {
                    navigateNext();
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                navigateNext();
            }
        });
    }

    public void updateSkipped() {
        navigateNext();
    }

    private void navigateNext() {
        if (userManager.getUserToken().isEmpty()) {
            view.navigateToLogin();
        } else {
            view.navigateToApp();
        }
    }

}
