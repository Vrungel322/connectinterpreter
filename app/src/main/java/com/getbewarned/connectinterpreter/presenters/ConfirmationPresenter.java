package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;
import android.provider.Settings;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.CodeReceived;
import com.getbewarned.connectinterpreter.interfaces.ConfirmationView;
import com.getbewarned.connectinterpreter.interfaces.HelpRequested;
import com.getbewarned.connectinterpreter.interfaces.LoginComplete;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.ProfileReceived;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.getbewarned.connectinterpreter.models.ProfileResponse;
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
            view.showError(view.getContext().getString(R.string.fields_required), null);
            return;
        }


        view.toggleEnabledLoginBtn(true);
        String deviceId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        networkManager.login(phone, code, deviceId, DeviceName.getDeviceName(), new LoginComplete() {
            @Override
            public void onLoginComplete(LoginResponse response) {
                if (response.isSuccess()) {
                    String name = response.getName();
                    userManager.updateUserName(name);
                    userManager.updateUserToken(response.getAuthToken());
                    userManager.updateUserSeconds(response.getSeconds());
                    userManager.updateUserActiveTill(response.getActiveTill());
                    userManager.updateUserUnlim(response.isUnlim());
                    userManager.updateUserUtog(response.isUtog());
                    userManager.updateFirstTime(response.getFirstTime());
                    userManager.updateUserPhone(response.getUserPhone());
                    userManager.updateUserRegion(response.getRegion());
                    getProfile();
                } else {
                    view.showError(response.getMessage(), null);
                    view.toggleEnabledRequestBtn(true);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage(), error.getCause());
                view.toggleEnabledRequestBtn(true);
            }
        });
    }

    private void getProfile() {
        networkManager.getProfile(new ProfileReceived() {
            @Override
            public void onReceived(ProfileResponse response) {
                ProfileResponse.Profile profile = response.getProfile();
                userManager.updateUserName(profile.getFirstName());
                userManager.updateUserLastName(profile.getLastName());
                userManager.updateUserPatronymic(profile.getPatronymic());
                userManager.updateUserCountry(profile.getCountry());
                userManager.updateUserCity(profile.getCity());

                if (profile.getFirstName() == null || profile.getFirstName().isEmpty()) {
                    view.navigateInputName();
                } else {
                    view.navigateToApp();
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage(), error.getCause());
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
                view.showError(error.getMessage(), error.getCause());
            }
        });
    }

    public void requestLoginHelp() {
        networkManager.loginHelp(phone, new HelpRequested() {
            @Override
            public void onHelpRequested() {
                view.showHelpRequested();
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage(), error.getCause());
            }
        });
    }

    public void wrongNumberPressed() {
        view.navigateBack();
    }
}
