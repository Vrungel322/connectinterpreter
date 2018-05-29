package com.getbewarned.connectinterpreter.services;

import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by artycake on 1/17/18.
 */

public class FBInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        UserManager userManager = new UserManager(this);
        if (userManager.getUserToken() == null) {
            return;
        }
        NetworkManager networkManager = new NetworkManager(this);
        networkManager.setAuthToken(userManager.getUserToken());
        networkManager.sendNotificationToken(refreshedToken);
    }
}
