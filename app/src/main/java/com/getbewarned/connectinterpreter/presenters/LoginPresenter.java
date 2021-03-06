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
import com.getbewarned.connectinterpreter.models.Country;
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
    private String phone;

    public LoginPresenter(LoginView view) {
        this.view = view;
        networkManager = new NetworkManager(view.getContext());
        userManager = new UserManager(view.getContext());
        view.setCountry(new Country(view.getContext().getString(R.string.ukraine), "+380"));
    }

    public void continuePressed(String phone, boolean accepted) {
        if (phone.isEmpty()) {
            view.showError(view.getContext().getString(R.string.fields_required));
            return;
        }
        if (!isValidPhone(phone)) {
            view.showError(view.getContext().getString(R.string.invalid_phone_format));
            return;
        }
        if (!accepted) {
            view.showError(view.getContext().getString(R.string.should_accept_privacy_policy));
            return;
        }

        this.phone = phone;
        view.confirmPhone(phone);

    }

    public void confirmedPressed() {
        view.navigateToConfirmation(phone);
    }

    public void countrySelected(Country country) {
        view.setCountry(country);
    }


    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^\\+[0-9]{10,15}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

}
