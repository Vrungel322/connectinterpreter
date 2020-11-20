package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.adapters.RequestsAdapter;
import com.getbewarned.connectinterpreter.interfaces.NewRequestCreated;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.RequestsView;
import com.getbewarned.connectinterpreter.interfaces.RequestsReceived;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.NewRequestResponse;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.models.RequestMessage;
import com.getbewarned.connectinterpreter.models.RequestResponse;
import com.getbewarned.connectinterpreter.models.RequestsResponse;
import com.getbewarned.connectinterpreter.ui.requests.RequestBitmapHolder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RequestsPresenter implements Presenter {

    private RequestsView view;
    private Context context;

    private Realm realm;
    private RequestsAdapter adapter;
    private NetworkManager networkManager;
    private UserManager userManager;

    private RealmResults<Request> requests;


    public RequestsPresenter(RequestsView view, Context context) {
        this.view = view;
        this.context = context;
        Realm.init(context);
        this.realm = Realm.getDefaultInstance();
        this.networkManager = new NetworkManager(context);
        this.userManager = new UserManager(context);
        this.networkManager.setAuthToken(userManager.getUserToken());
    }

    @Override
    public void onCreate(Bundle extras) {
        this.requests = this.realm.where(Request.class).sort("created", Sort.DESCENDING).findAll();
        this.adapter = new RequestsAdapter(requests, true);
        this.adapter.setOnRequestSelected(new RequestsAdapter.OnRequestSelected() {
            @Override
            public void onRequestSelected(Request request) {
                view.goToRequest(request);
            }
        });
        view.toggleListVisibility(requests.size() > 0);
        this.loadRequests();
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

    public void createRequestPressed() {
        view.openImagePicker();
    }

    public RequestsAdapter getAdapter() {
        return adapter;
    }

    private void loadRequests() {
        this.networkManager.loadRequests(new RequestsReceived() {
            @Override
            public void onRequestsReceived(RequestsResponse response) {
                for (RequestResponse requestResponse: response.getRequests()) {
                    realm.beginTransaction();
                    boolean isNew = false;
                    Request request = realm.where(Request.class).equalTo("id", requestResponse.getId()).findFirst();
                    if (request == null) {
                        request = new Request();
                        isNew = true;
                        request.setId(requestResponse.getId());
                    }
                    request.setName(requestResponse.getName());
                    request.setStatus(requestResponse.getStatus());
                    request.setCreated(new Date(requestResponse.getCreated_at() * 1000));
                    request.setUpdated(new Date(requestResponse.getUpdated_at() * 1000));
                    if (isNew) {
                        realm.copyToRealm(request);
                    }
                    realm.commitTransaction();
                }
                view.toggleListVisibility(requests.size() > 0);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });
    }
}
