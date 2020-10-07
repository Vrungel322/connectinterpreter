package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;

import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.CompensationView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.ui.compensation.data.CompensationDataHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompensationPresenter implements Presenter {

    private final CompensationView view;
    private NetworkManager networkManager;
    private UserManager userManager;

    public CompensationPresenter(CompensationView view) {
        this.view = view;
        userManager = new UserManager(view.getContext());
        networkManager = new NetworkManager(view.getContext());
        networkManager.setAuthToken(userManager.getUserToken());
    }

    public void updateCompensationInfo() {
        CompensationDataHolder dataHolder = CompensationDataHolder.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date(dataHolder.dateOfBirthMillis);
        String birth = dateFormat.format(date);
        String passport = dataHolder.passportSerialCode + dataHolder.passportSerialNumber;
        networkManager.updateCompensationInfo(dataHolder.firstName, dataHolder.lastName,
                dataHolder.patronymic, birth, passport, dataHolder.taxPayerId,
                dataHolder.insuranceId, dataHolder.city, dataHolder.streetAddress,
                dataHolder.apartmentNumber, dataHolder.apartmentNumber, dataHolder.postId,
                new BaseRequestCompleted() {
                    @Override
                    public void onComplete(ApiResponseBase response) {
                        if (response.isSuccess()) {
                            view.goBack();
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


