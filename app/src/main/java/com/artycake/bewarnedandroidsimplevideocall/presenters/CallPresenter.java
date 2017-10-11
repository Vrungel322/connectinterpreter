package com.artycake.bewarnedandroidsimplevideocall.presenters;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.artycake.bewarnedandroidsimplevideocall.interfaces.CallView;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.Presenter;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.TokenReceived;
import com.artycake.bewarnedandroidsimplevideocall.managers.FirebaseManager;
import com.artycake.bewarnedandroidsimplevideocall.managers.NetworkManager;
import com.artycake.bewarnedandroidsimplevideocall.models.FirebaseTrialMinutes;
import com.artycake.bewarnedandroidsimplevideocall.models.FirebaseVideoCall;
import com.artycake.bewarnedandroidsimplevideocall.models.HumanTime;
import com.artycake.bewarnedandroidsimplevideocall.models.OpenTokTokenResponse;
import com.google.firebase.database.DataSnapshot;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import java.util.Locale;


/**
 * Created by artycake on 10/11/17.
 */

public class CallPresenter implements Presenter, Session.SessionListener, PublisherKit.PublisherListener {
    private static final String TAG = CallPresenter.class.getSimpleName();
    public static final String LEFT_MILLIS = "left_millis";

    private CallView view;
    private FirebaseManager firebaseManager;
    private NetworkManager networkManager;

    private Session session;
    private Publisher publisher;
    private Subscriber subscriber;

    private FirebaseVideoCall call;
    private boolean answered = false;
    private CountDownTimer countDownTimer;

    private long leftMinutesOnStart;
    private FirebaseTrialMinutes trialMinutes;

    public CallPresenter(CallView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
        this.networkManager = new NetworkManager(view.getContext());
        DataSnapshot dataSnapshot = firebaseManager.getCurrentSnapshot();
        if (dataSnapshot == null) {
            view.navigateBack();
            return;
        }

        trialMinutes = dataSnapshot.getValue(FirebaseTrialMinutes.class);
        trialMinutes.setKey(dataSnapshot.getKey());
        leftMinutesOnStart = trialMinutes.getMinutes();
    }

    @Override
    public void onCreate(Bundle extras) {
        view.toggleEndCallButtonVisibility(false);
        view.updateLeftTime(new HumanTime(leftMinutesOnStart).getTime());
        view.updateCurrentCallDuration("00:00");
        view.requestPermissions();
    }

    public void onPermissionsGranted() {
        view.showIndicator();
        makeCall();
    }

    private void makeCall() {
        networkManager.getToken(new TokenReceived() {
            @Override
            public void onTokenReceived(OpenTokTokenResponse response) {
                createSession(response.getApiKey(), response.getSessionId(), response.getToken());
            }

            @Override
            public void onErrorReceived(Error error) {
                error.printStackTrace();
                view.hideIndicator();
                view.showError(error.getMessage()); // TODO: normal message
            }
        });
    }

    private void createSession(String apiKey, String sessionId, String token) {
        session = new Session.Builder(view.getContext(), apiKey, sessionId).build();
        session.setSessionListener(CallPresenter.this);
        session.connect(token);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        endCall();
    }

    public void endCall() {
        if (!answered && call != null) {
            firebaseManager.cancelCall(call);
        }
        if (session != null) {
            session.disconnect();
            session.unsubscribe(subscriber);
            session.unpublish(publisher);
        }

    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(view.getContext()).build();
        publisher.setPublisherListener(CallPresenter.this);

        view.showSelfView(publisher.getView());

        session.publish(publisher);

        call = firebaseManager.makeCall(session.getSessionId());

    }

    @Override
    public void onDisconnected(Session session) {
        countDownTimer.cancel();
        view.navigateBack();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null) {
            subscriber = new com.opentok.android.Subscriber.Builder(view.getContext(), stream).build();
            session.subscribe(subscriber);
            view.showInterpreterView(subscriber.getView());
            connectionEstablished();
        }
    }

    private void connectionEstablished() {
        answered = true;
        runTimer();
        view.hideIndicator();
        view.toggleEndCallButtonVisibility(true);
    }

    private void runTimer() {
        if (countDownTimer != null) {
            return;
        }
        countDownTimer = new CountDownTimer(leftMinutesOnStart, 1000) {

            public void onTick(long millisUntilFinished) {
                long timePassed = leftMinutesOnStart - millisUntilFinished;
                view.updateLeftTime(new HumanTime(millisUntilFinished).getTime());
                view.updateCurrentCallDuration(new HumanTime(timePassed).getTime());
                trialMinutes.setMinutes(millisUntilFinished);
                firebaseManager.updateLeftMinutes(trialMinutes);
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
        view.hideIndicator();
        Log.d(TAG, opentokError.getMessage());
        view.showError(opentokError.getMessage());
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
        view.hideIndicator();
        Log.d(TAG, opentokError.getMessage());
        view.showError(opentokError.getMessage());
    }
}
