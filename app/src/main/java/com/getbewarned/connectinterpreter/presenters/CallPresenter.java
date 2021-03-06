package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.getbewarned.connectinterpreter.interfaces.CallView;
import com.getbewarned.connectinterpreter.interfaces.MessageSent;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.HumanTime;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;


/**
 * Created by artycake on 10/11/17.
 */

public class CallPresenter implements Presenter, Session.SessionListener, PublisherKit.PublisherListener {
    private static final String TAG = CallPresenter.class.getSimpleName();
    public static final String TOKEN_EXTRA = "CallPresenter.token";
    public static final String SESSION_EXTRA = "CallPresenter.session_id";
    public static final String KEY_EXTRA = "CallPresenter.api_key";
    public static final String SECONDS_EXTRA = "CallPresenter.max_seconds";

    public static final Long PLEASE_WAITE_VIDEO_DELAY_MILLIS = 5 * 60 * 1000L;

    private final CallView view;
    private final UserManager userManager;
    private final NetworkManager networkManager;

    private Session session;
    private Publisher publisher;
    private Subscriber subscriber;

    public boolean answered = false;
    private CountDownTimer countDownTimer;
    private boolean ended = false;

    private long leftMinutesOnStart;


    private String apiKey;
    private String sessionId;
    private String token;

    private final Handler handler = new Handler();


    public CallPresenter(CallView view) {
        this.view = view;
        this.userManager = new UserManager(view.getContext());
        this.networkManager = new NetworkManager(view.getContext());
        this.networkManager.setAuthToken(userManager.getUserToken());

    }

    @Override
    public void onCreate(Bundle extras) {
        view.toggleEndCallButtonVisibility(false);
        view.updateCurrentCallDuration("00:00");
        view.showIndicator(PLEASE_WAITE_VIDEO_DELAY_MILLIS);
        apiKey = extras.getString(KEY_EXTRA);
        sessionId = extras.getString(SESSION_EXTRA);
        token = extras.getString(TOKEN_EXTRA);
        leftMinutesOnStart = extras.getLong(SECONDS_EXTRA) * 1000;

        view.updateLeftTime(new HumanTime(view.getContext(), leftMinutesOnStart).getTime());
        createSession(apiKey, sessionId, token);


    }

    private void createSession(String apiKey, String sessionId, String token) {
        session = new Session.Builder(view.getContext(), apiKey, sessionId).build();
        session.setSessionListener(CallPresenter.this);
        session.connect(token);
    }

    @Override
    public void onPause() {
        if (session != null) {
            session.onPause();
        }
        Log.d("TAG", "onPause");
    }

    @Override
    public void onResume() {
        if (session != null) {
            session.onResume();
        }
        Log.d("TAG", "onResume");
    }

    @Override
    public void onDestroy() {
        endCall();
    }

    public void endCall() {
        if (ended) {
            return;
        }

        ended = true;
        if (session != null) {
            session.disconnect();
            session.setSessionListener(null);
            if (subscriber != null) {
                subscriber.setSubscriberListener(null);
                session.unsubscribe(subscriber);
                view.resetVolume();
            }
            if (publisher != null) {
                publisher.setPublisherListener(null);
                session.unpublish(publisher);
            }
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        view.navigateBack();
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(view.getContext()).build();
        publisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FIT);
        publisher.setPublisherListener(CallPresenter.this);
        view.showSelfView(publisher.getView());

        session.publish(publisher);
    }


    @Override
    public void onDisconnected(Session session) {
        endCall();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null) {
            subscriber = new Subscriber.Builder(view.getContext(), stream).build();
            subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FIT);
            session.subscribe(subscriber);
            view.showInterpreterView(subscriber.getView());
            connectionEstablished();
            subscriber.setAudioStatsListener(new SubscriberKit.AudioStatsListener() {
                @Override
                public void onAudioStats(SubscriberKit subscriberKit, SubscriberKit.SubscriberAudioStats subscriberAudioStats) {
                    view.setMaxVolume();
                    subscriber.setAudioStatsListener(null);
                }
            });
        }
    }

    private void connectionEstablished() {
        answered = true;
        runTimer();
        view.hideIndicator();
        view.toggleEndCallButtonVisibility(true);
        userManager.updateLastCallSessionId(session.getSessionId());

    }

    private void runTimer() {
        if (countDownTimer != null) {
            return;
        }
        countDownTimer = new CountDownTimer(leftMinutesOnStart, 1000) {

            public void onTick(long millisUntilFinished) {
                long timePassed = leftMinutesOnStart - millisUntilFinished;
                view.updateLeftTime(new HumanTime(view.getContext(), millisUntilFinished).getTime());
                view.updateCurrentCallDuration(new HumanTime(view.getContext(), timePassed).getTime());
            }

            public void onFinish() {
                session.disconnect();
                view.updateLeftTime("00:00");
                countDownTimer = null;
            }
        }.start();
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        session.disconnect();
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.d(TAG, opentokError.getMessage());
        if (ended) {
            return;
        }
        view.hideIndicator();
        view.showErrorNewUI(opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        session.disconnect();
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.d(TAG, opentokError.getMessage());
        if (ended) {
            return;
        }
        view.hideIndicator();
        view.showErrorNewUI(opentokError.getMessage());
    }

    public void sendMessage(final String message) {
        if (message.trim().isEmpty()) {
            return;
        }
        networkManager.sendMessage(session.getSessionId(), message.trim(), new MessageSent() {
            @Override
            public void onMessageSent(ApiResponseBase response) {
                if (response.isSuccess()) {
                    view.showOneMoreMessage(message.trim());
                }
                // else show failed to send
            }

            @Override
            public void onErrorReceived(Error error) {
                // show failed to send
            }
        });
    }

}
