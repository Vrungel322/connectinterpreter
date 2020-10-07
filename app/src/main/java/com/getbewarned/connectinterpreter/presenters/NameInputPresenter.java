package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;

import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.NameInputView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.NameResponse;

public class NameInputPresenter implements Presenter {

    private final NameInputView view;
    private NetworkManager networkManager;
    private UserManager userManager;

    public NameInputPresenter(NameInputView view) {
        this.view = view;
        userManager = new UserManager(view.getContext());
        networkManager = new NetworkManager(view.getContext());
        networkManager.setAuthToken(userManager.getUserToken());
    }

    public void continueClicked(String name) {
        networkManager.updateName(name, new NameChanged() {
            @Override
            public void onNameChanged(NameResponse response) {
                if (response.isSuccess()) {
                    String name = response.getName();
                    userManager.updateUserName(name);
                    view.navigateToApp();
                } else {
                    view.showError(response.getMessage(), null);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage(), error.getCause());
            }
        });
    }

    @Override
    public void onCreate(Bundle extras) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

}
