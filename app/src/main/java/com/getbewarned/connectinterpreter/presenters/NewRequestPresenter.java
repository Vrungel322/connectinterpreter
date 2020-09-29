package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.interfaces.NewRequestCreated;
import com.getbewarned.connectinterpreter.interfaces.NewRequestView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.NewRequestResponse;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.models.RequestMessage;

import java.util.Date;

import io.realm.Realm;

public class NewRequestPresenter implements Presenter {

    private Realm realm;
    private NewRequestView view;
    NetworkManager networkManager;

    public NewRequestPresenter(final NewRequestView view, Context context) {
        this.view = view;
        this.realm = Realm.getDefaultInstance();
        UserManager userManager = new UserManager(context);
        networkManager = new NetworkManager(context);
        networkManager.setAuthToken(userManager.getUserToken());
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

    public void createRequest(Bitmap bitmap) {
        view.showLoading();
        this.networkManager.newRequest(bitmap, new NewRequestCreated() {
            @Override
            public void onRequestCreated(NewRequestResponse response) {
                realm.beginTransaction();
                Request request = new Request();
                request.setId(response.getRequest().getId());
                request.setCreated(new Date(response.getRequest().getCreated_at() * 1000));
                request.setUpdated(new Date(response.getRequest().getUpdated_at() * 1000));
                request.setStatus(response.getRequest().getStatus());
                request.setName(response.getRequest().getName());
                request = realm.copyToRealmOrUpdate(request);
                RequestMessage message = new RequestMessage();
                message.setId(response.getRequestMessage().getId());
                message.setCreated(new Date(response.getRequestMessage().getCreated_at() * 1000));
                message.setType(response.getRequestMessage().getType());
                message.setContent(response.getRequestMessage().getContent());
                message.setThumbnail(response.getRequestMessage().getThumbnail());
                message.setAuthor(RequestMessage.SELF);
                request.getMessages().add(message);
                realm.commitTransaction();
                view.hideLoading();
                view.goToRequest(request);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.hideLoading();
                view.showError(error.getMessage());
            }
        });
    }
}
