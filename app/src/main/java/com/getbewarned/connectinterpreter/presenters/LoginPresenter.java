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

    public void continuePressed(String phone) {
        if (phone.isEmpty()) {
            view.showError(view.getContext().getString(R.string.fields_required));
            return;
        }
        if (!isValidPhone(phone)) {
            view.showError(view.getContext().getString(R.string.invalid_phone_format));
            return;
        }
        view.navigateToConfirmation(phone);
    }


    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^\\+[0-9]{10,15}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
