package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.GroupSessionReceived;
import com.getbewarned.connectinterpreter.interfaces.GroupSessionView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.GroupSessionResponse;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Date;

public class GroupSessionPresenter implements Presenter, Session.SessionListener, PublisherKit.PublisherListener {

    public static final String SESSION_ID = "session_id";

    private GroupSessionView view;
    private Context context;

    private NetworkManager networkManager;

    private String groupSessionId;
    private Session session;
    private Subscriber subscriber;
    private Subscriber asker;
    private Publisher publisher;

    private Socket socket;

    private int clientId;
    private boolean leaving = false;
    private boolean asking = false;


    public GroupSessionPresenter(GroupSessionView view, Context context) {
        this.view = view;
        this.context = context;
        this.networkManager = new NetworkManager(context);
        UserManager userManager = new UserManager(context);
        networkManager.setAuthToken(userManager.getUserToken());
        try {
            this.socket = IO.socket("http://node58416-env-6191050.mircloud.ru:11017");
            this.socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle extras) {
        String sessionId = extras.getString(SESSION_ID);
        if (sessionId == null) {
            view.showError("Wrong session link");
            return;
        }
        if (this.socket != null) {
            this.socket.on("interpreter-group." + sessionId + ":asker.answer", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        int answeringId = data.getInt("client_id");
                        if (answeringId == clientId) {
                            view.runOnUi(new Runnable() {
                                @Override
                                public void run() {
                                    publishSelfVideo();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.socket.on("interpreter-group." + sessionId + ":asker.stop", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        int answeringId = data.getInt("client_id");
                        if (answeringId == clientId) {
                            view.runOnUi(new Runnable() {
                                @Override
                                public void run() {
                                    toggleAsking();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.socket.on("interpreter-group." + sessionId + ":group.closed", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    view.runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            leave();
                        }
                    });
                }
            });
        }
        this.groupSessionId = sessionId;

        reconnect();
    }

    private void publishSelfVideo() {
        publisher = new Publisher.Builder(context)
                .resolution(Publisher.CameraCaptureResolution.LOW)
                .frameRate(Publisher.CameraCaptureFrameRate.FPS_30)
                .build();
        publisher.setPublisherListener(this);
        view.updateAskerView(publisher.getView());

        session.publish(publisher);
    }


    private void connectToSession(GroupSessionResponse response) {
        session = new Session.Builder(context, response.getApiKey(), response.getSessionId()).build();
        session.setSessionListener(this);
        session.connect(response.getToken());
    }

    public void reconnect() {
        view.toggleLoading(true);
        networkManager.connectToGroupSession(groupSessionId, new GroupSessionReceived() {
            @Override
            public void onDataReceived(GroupSessionResponse response) {
                view.toggleLoading(false);
                clientId = response.getClient_id();
                view.showSessionInfo(response.getEventName(), new Date(response.getEventDate()), response.canConnect());
                if (response.canConnect()) {
                    connectToSession(response);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.toggleLoading(false);
                view.showError(error.getMessage());
            }
        });
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (session != null) {
            if (publisher != null) {
                session.unpublish(publisher);
            }
            if (subscriber != null) {
                session.unsubscribe(subscriber);
            }
            if (asker != null) {
                session.unsubscribe(asker);
            }
            session.disconnect();
        }
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
        leave();
    }


    @Override
    public void onConnected(Session session) {

    }

    @Override
    public void onDisconnected(Session session) {
        if (leaving) {
            view.leave();
            return;
        }
        view.showReconnect();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (getStreamType(stream).equals("interpreter")) {
            if (subscriber == null) {
                subscriber = new Subscriber.Builder(context, stream).build();
                session.subscribe(subscriber);
                subscriber.setSubscribeToAudio(false);
                view.updateInterpreterView(subscriber.getView());
            }
        }
        if (getStreamType(stream).equals("listener")) {
            if (asker != null) {
                session.unsubscribe(asker);
            }
            asker = new Subscriber.Builder(context, stream).build();
            session.subscribe(asker);
            asker.setSubscribeToAudio(false);
            view.updateAskerView(asker.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (getStreamType(stream).equals("interpreter")) {
            session.unsubscribe(subscriber);
            subscriber = null;
            view.updateInterpreterView(null);
        }
        if (getStreamType(stream).equals("listener")) {
            if (asker != null) {
                session.unsubscribe(asker);
                asker = null;
                view.updateAskerView(null);
            }
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        if (leaving) {
            return;
        }
        view.toggleLoading(false);
        view.showError(opentokError.getMessage());
    }

    public void leave() {
        if (leaving) {
            return;
        }
        leaving = true;
        if (session != null) {
            session.disconnect();
        } else {
            view.leave();
        }
    }

    public void toggleAsking() {
        if (asking) {
            if (publisher != null) {
                session.unpublish(publisher);
                publisher = null;
                view.toggleAsking(false);
                view.updateAskerView(null);
            } else {
                networkManager.stopAsking(groupSessionId, new BaseRequestCompleted() {
                    @Override
                    public void onComplete(ApiResponseBase response) {
                        view.toggleAsking(false);
                        asking = false;
                    }

                    @Override
                    public void onErrorReceived(Error error) {
                        view.toggleAsking(false);
                        asking = false;
                    }
                });
            }
        } else {
            networkManager.askQuestion(groupSessionId, new BaseRequestCompleted() {
                @Override
                public void onComplete(ApiResponseBase response) {
                    view.toggleAsking(true);
                    asking = true;
                }

                @Override
                public void onErrorReceived(Error error) {
                }
            });
        }
    }

    private String getStreamType(Stream stream) {
        try {
            JSONObject jsonObject = new JSONObject(stream.getConnection().getData());
            return jsonObject.getString("type");
        } catch (JSONException e) {
            return "";
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        if (publisher != null && asker == null) {
            view.updateAskerView(null);
            publisher = null;
        }
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
