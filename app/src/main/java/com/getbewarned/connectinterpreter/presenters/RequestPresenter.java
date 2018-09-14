package com.getbewarned.connectinterpreter.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.adapters.RequestMessagesAdapter;
import com.getbewarned.connectinterpreter.interfaces.NewRequestMessageCreated;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.RequestMessagesReceived;
import com.getbewarned.connectinterpreter.interfaces.RequestView;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.MessagesResponse;
import com.getbewarned.connectinterpreter.models.NewMessageResponse;
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.models.RequestMessage;
import com.getbewarned.connectinterpreter.models.RequestMessageResponse;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Date;

import io.realm.Realm;

public class RequestPresenter implements Presenter {

    public static final String REQUEST_ID = "request_id";

    private RequestView view;
    private Context context;
    private Realm realm;
    private NetworkManager networkManager;
    private UserManager userManager;

    private RequestMessagesAdapter adapter;
    private Request request;
    private Socket socket;

    BroadcastReceiver newMessageReceiver;
    boolean receiverRegistered = false;

    public RequestPresenter(RequestView view, Context context) {
        this.view = view;
        this.context = context;
        Realm.init(context);
        this.realm = Realm.getDefaultInstance();
        this.networkManager = new NetworkManager(context);
        this.userManager = new UserManager(context);
        networkManager.setAuthToken(userManager.getUserToken());
        newMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras().getLong(REQUEST_ID) == request.getId()) {
                    loadMessages();
                }
            }
        };

        try {
            this.socket = IO.socket("http://node58416-env-6191050.mircloud.ru:11017");
            this.socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle extras) {
        long requestId = extras.getLong(REQUEST_ID);
        this.request = realm.where(Request.class).equalTo("id", requestId).findFirst();
        if (request == null) {
            view.goBack();
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                request.setUnreadCount(0);
            }
        });
        view.setTitle(request.getName());
        adapter = new RequestMessagesAdapter(request.getMessages(), true);
        adapter.setItemClickListener(new RequestMessagesAdapter.ItemClickListener() {
            @Override
            public void onItemSelected(RequestMessage message) {
                if (message.getType().equals("image")) {
                    view.showImagePreview(message.getContent());
                } else if (message.getType().equals("video")) {
                    view.showVideoPreview(message.getContent());
                }
            }
        });
    }

    private void loadMessages() {
        this.networkManager.loadRequestMessages(request, new RequestMessagesReceived() {
            @Override
            public void onMessagesReceived(MessagesResponse response) {
                for (RequestMessageResponse messageResponse : response.getMessages()) {
                    realm.beginTransaction();
                    boolean shouldAdd = false;
                    RequestMessage message = request.getMessages().where().equalTo("id", messageResponse.getId()).findFirst();
                    if (message == null) {
                        message = new RequestMessage();
                        shouldAdd = true;
                        message.setId(messageResponse.getId());
                    }
                    message.setCreated(new Date(messageResponse.getCreated_at() * 1000));
                    message.setType(messageResponse.getType());
                    message.setContent(messageResponse.getContent());
                    message.setThumbnail(messageResponse.getThumbnail());
                    message.setAuthor(messageResponse.getAuthorType().equals("client") ? RequestMessage.SELF : RequestMessage.INTERPRETER);

                    if (shouldAdd) {
                        request.getMessages().add(message);
                    }

                    realm.commitTransaction();
                    adapter.notifyDataSetChanged();
                    view.scrollToBottom();

                }
            }

            @Override
            public void onErrorReceived(Error error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        if (receiverRegistered) {
            context.unregisterReceiver(newMessageReceiver);
            receiverRegistered = false;
        }
    }

    @Override
    public void onResume() {
        this.loadMessages();
        this.request = realm.where(Request.class).equalTo("id", request.getId()).findFirst();
        if (this.request.getStatus().equals("closed")) {
            view.hideSendButtons();
        }
        if (!receiverRegistered) {
            context.registerReceiver(newMessageReceiver, new IntentFilter(context.getApplicationInfo().packageName + ".UPDATE_REQUEST_MESSAGES"));
            receiverRegistered = true;
        }
    }

    @Override
    public void onDestroy() {

    }

    public void imageSelected(Bitmap image) {
        view.showLoading();
        this.networkManager.newRequestMessage(image, request, new NewRequestMessageCreated() {
            @Override
            public void onMessageCreated(final NewMessageResponse response) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RequestMessage message = new RequestMessage();
                        message.setId(response.getRequestMessage().getId());
                        message.setCreated(new Date(response.getRequestMessage().getCreated_at() * 1000));
                        message.setAuthor(RequestMessage.SELF);
                        message.setContent(response.getRequestMessage().getContent());
                        message.setThumbnail(response.getRequestMessage().getThumbnail());
                        message.setType(response.getRequestMessage().getType());
                        request.getMessages().add(message);
                    }
                });
                adapter.notifyDataSetChanged();
                view.hideLoading();
                view.scrollToBottom();
            }

            @Override
            public void onErrorReceived(Error error) {
                view.hideLoading();
            }
        });
    }

    public RequestMessagesAdapter getAdapter() {
        return adapter;
    }
}
