package com.getbewarned.connectinterpreter.presenters;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.UtogResponseReceived;
import com.getbewarned.connectinterpreter.interfaces.UtogView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.UtogResponse;

/**
 * Created by artycake on 2/2/18.
 */

public class UtogPresenter {

    private UtogView view;
    private NetworkManager networkManager;

    private boolean sending = false;

    public UtogPresenter(UtogView view) {
        this.view = view;
        this.networkManager = new NetworkManager(view.getContext());
        UserManager userManager = new UserManager(view.getContext());
        this.networkManager.setAuthToken(userManager.getUserToken());
    }

    public void cancelPressed() {
        view.navigateBack();
    }

    public void continuePressed(String firstName, String lastName, String patronymic, String memberId) {
        if (sending) {
            return;
        }
        if (firstName.isEmpty() || lastName.isEmpty() || patronymic.isEmpty() || memberId.isEmpty()) {
            view.showError(view.getContext().getString(R.string.fields_required));
            return;
        }
        sending = true;

        networkManager.sendUtogInfo(firstName, lastName, patronymic, memberId, new UtogResponseReceived() {
            @Override
            public void onInfoReceived(UtogResponse response) {
                sending = false;
                if (response.isSuccess()) {
                    view.showSuccess(response.getMessage());
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                sending = false;
                view.showError(error.getMessage());
            }
        });

    }

    public void successOkPressed() {
        view.navigateBack();
    }

}
