package com.getbewarned.connectinterpreter.presenters;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.interfaces.AppVersionReceived;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.SplashView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.ProxyManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.AppVersionResponse;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

import io.realm.Realm;

/**
 * Created by artycake on 1/24/18.
 */

public class SplashPresenter implements Presenter {

    private SplashView view;
    private NetworkManager networkManager;
    private UserManager userManager;
    private boolean linkOpening = false;

    public SplashPresenter(SplashView view) {
        this.view = view;
        ProxyManager proxyManager = new ProxyManager(view.getContext());
        ProxySelector.setDefault(proxyManager);
        networkManager = new NetworkManager(view.getContext());
        userManager = new UserManager(view.getContext());
        Realm.init(view.getContext());
    }

    @Override
    public void onCreate(Bundle extras) {
        if (extras.getString("link") != null) {
            linkOpening = true;
            Intent linkIntent = new Intent(Intent.ACTION_VIEW);
            linkIntent.setData(Uri.parse(extras.getString("link")));
            view.getContext().startActivity(linkIntent);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        if (linkOpening) {
            linkOpening = false;
            return;
        }

        navigateNext();
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
                } else if (userManager.getLastAppVersion() != response.getBuildNumber() && response.getDescription() != null) {
                    showVersionInfo(response);
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

    private void showVersionInfo(AppVersionResponse response) {
        userManager.updateLastAppVersion(response.getBuildNumber());
//        don't show in ржя
//        view.showNewVersionInfo(response.getDescription());
    }

    public void updateSkipped() {
        navigateNext();
    }

    public void infoAccepted() {
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
