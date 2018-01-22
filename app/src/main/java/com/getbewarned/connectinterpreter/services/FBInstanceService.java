package com.getbewarned.connectinterpreter.services;

import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by artycake on 1/17/18.
 */

public class FBInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        UserManager userManager = new UserManager(this);
        if (userManager.getUserToken() == null) {
            return;
        }
        NetworkManager networkManager = new NetworkManager(this);
        networkManager.setAuthToken(userManager.getUserToken());
        networkManager.sendNotificationToken(refreshedToken);
    }
}
