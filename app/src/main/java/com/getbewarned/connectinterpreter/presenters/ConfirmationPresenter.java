package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;
import android.provider.Settings;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.CodeReceived;
import com.getbewarned.connectinterpreter.interfaces.ConfirmationView;
import com.getbewarned.connectinterpreter.interfaces.LoginComplete;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.jaredrummler.android.device.DeviceName;

/**
 * Created by artycake on 1/24/18.
 */

public class ConfirmationPresenter implements Presenter {

    public static final String PHONE_EXTRA = "ConfirmationPresenter.phone";

    private ConfirmationView view;
    private NetworkManager networkManager;
    private UserManager userManager;

    private String phone;

    public ConfirmationPresenter(ConfirmationView view) {
        this.view = view;
        networkManager = new NetworkManager(view.getContext());
        userManager = new UserManager(view.getContext());
    }

    @Override
    public void onCreate(Bundle extras) {
        phone = extras.getString(PHONE_EXTRA);
        if (phone == null || phone.isEmpty()) {
            view.navigateBack();
            return;
        }
        view.showNumber(phone);
        requestCode();
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

    public void loginPressed(String code) {
        if (code.isEmpty()) {
            view.showError(view.getContext().getString(R.string.fields_required));
            return;
        }


        view.toggleEnabledLoginBtn(true);
        String deviceId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        networkManager.login(phone, code, deviceId, DeviceName.getDeviceName(), new LoginComplete() {
            @Override
            public void onLoginComplete(LoginResponse response) {
                if (response.isSuccess()) {
                    userManager.updateUserName(response.getName());
                    userManager.updateUserToken(response.getAuthToken());
                    userManager.updateUserSeconds(response.getSeconds());
                    userManager.updateUserActiveTill(response.getActiveTill());
                    userManager.updateUserUnlim(response.isUnlim());
                    userManager.updateUserUtog(response.isUtog());
                    userManager.updateUserUkrainian(response.isUkrainian());
                    view.navigateToApp();
                } else {
                    view.showError(response.getMessage());
                    view.toggleEnabledRequestBtn(true);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
                view.toggleEnabledRequestBtn(true);
            }
        });
    }

    private void requestCode() {
        networkManager.getCode(phone, new CodeReceived() {
            @Override
            public void onCodeReceived(ApiResponseBase response) {
                if (response.getCode() != 0) {
                    view.setCode(String.valueOf(response.getCode()));
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void wrongNumberPressed() {
        view.navigateBack();
    }
}
