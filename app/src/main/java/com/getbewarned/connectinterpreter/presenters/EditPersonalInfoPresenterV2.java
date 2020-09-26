package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.EditPersonalInfoView;
import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.NameResponse;

import io.realm.Realm;

public class EditPersonalInfoPresenterV2 implements Presenter {
    private NetworkManager networkManager;
    private UserManager userManager;
    private EditPersonalInfoView view;


    public EditPersonalInfoPresenterV2(final EditPersonalInfoView view, Context context) {
        this.view = view;
        userManager = new UserManager(context);
        networkManager = new NetworkManager(context);
        networkManager.setAuthToken(userManager.getUserToken());
    }

    @Override
    public void onCreate(Bundle extras) {
        view.showUsedData(userManager.getUserPhone(),
                userManager.getUserName(), userManager.getUserLastName(), userManager.getUserPatronymic(),
                userManager.getUserCountry(), userManager.getUserCity());
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

    public void logout() {
        networkManager.logout(new BaseRequestCompleted() {
            @Override
            public void onComplete(ApiResponseBase response) {
                if (response.isSuccess()) {
                    view.navigateToLogin();
                    userManager.updateUserToken(null);
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    });
                } else {
                    view.navigateToLogin();
                    userManager.updateUserToken(null);

                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.navigateToLogin();
                userManager.updateUserToken(null);

            }
        });
    }

    public void saveUserData(String name, String lastName, String patronimic, String country, String city, final DataStored dataStored) {
//        userManager.updateUserName(name);
        userManager.updateUserLastName(lastName);
        userManager.updateUserPatronymic(patronimic);
        userManager.updateUserCountry(country);
        userManager.updateUserCity(city);

        if (!name.isEmpty()) {
            networkManager.updateName(name, new NameChanged() {
                @Override
                public void onNameChanged(NameResponse response) {
                    userManager.updateUserName(response.getName());
                    dataStored.dataStored();
                }

                @Override
                public void onErrorReceived(Error error) {
                    Log.d("saveUserData", "onErrorReceived: " + error.toString());
                }
            });
        }else {
            dataStored.dataStored();
        }
    }
}
