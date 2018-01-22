package com.getbewarned.connectinterpreter.presenters;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.CodeReceived;
import com.getbewarned.connectinterpreter.interfaces.LoginComplete;
import com.getbewarned.connectinterpreter.interfaces.LoginView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaredrummler.android.device.DeviceName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artycake on 1/9/18.
 */

public class LoginPresenter {

    private LoginView view;
    private NetworkManager networkManager;
    private CountDownTimer countDownTimer;
    private UserManager userManager;

    public LoginPresenter(LoginView view) {
        this.view = view;
        networkManager = new NetworkManager(view.getContext());
        userManager = new UserManager(view.getContext());
    }

    public void getCode(String phone) {
        if (phone.isEmpty()) {
            // show error
            return;
        }
        Pattern pattern = Pattern.compile("^\\+[0-9]{10,15}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            return;
        }
        view.toggleEnabledRequestBtn(false);
        networkManager.getCode(phone, new CodeReceived() {
            @Override
            public void onCodeReceived(ApiResponseBase response) {
                if (response.isSuccess()) {
                    view.showPasswordAndLoginBtn();
                    view.setCode(String.valueOf(response.getCode()));
                    view.updateHint(view.getContext().getString(R.string.login_code_delay, 30));
                    countDownTimer = new CountDownTimer(30 * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            view.updateHint(view.getContext().getString(R.string.login_code_delay, l / 1000));
                        }

                        @Override
                        public void onFinish() {
                            view.toggleEnabledRequestBtn(true);
                            view.updateHint(view.getContext().getString(R.string.login_code_helper));
                            countDownTimer = null;
                        }
                    };
                    countDownTimer.start();
                } else {
                    Toast.makeText(view.getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    view.toggleEnabledRequestBtn(true);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                view.toggleEnabledRequestBtn(true);
            }
        });
    }

    public void login(String phone, String code) {
        if (phone.isEmpty() || code.isEmpty()) {
            // show error
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
                    userManager.updateUserSeconds(response.getMilliseconds());
                    view.navigateToApp();
                } else {
                    Toast.makeText(view.getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    view.toggleEnabledRequestBtn(true);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                view.toggleEnabledRequestBtn(true);
            }
        });

    }

}
