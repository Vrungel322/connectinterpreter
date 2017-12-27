package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.CallView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.TokenReceived;
import com.getbewarned.connectinterpreter.managers.FirebaseManager;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.FirebaseTrialMinutes;
import com.getbewarned.connectinterpreter.models.FirebaseVideoCall;
import com.getbewarned.connectinterpreter.models.HumanTime;
import com.getbewarned.connectinterpreter.models.OpenTokTokenResponse;
import com.google.firebase.database.DataSnapshot;
import com.jaredrummler.android.device.DeviceName;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by artycake on 10/11/17.
 */

public class CallPresenter implements Presenter, Session.SessionListener, PublisherKit.PublisherListener {
    private static final String TAG = CallPresenter.class.getSimpleName();
    public static final String REASON_EXTRA = "CallPresenter.reason";

    private CallView view;
    private FirebaseManager firebaseManager;
    private NetworkManager networkManager;

    private Session session;
    private Publisher publisher;
    private Subscriber subscriber;

    private FirebaseVideoCall call;
    private boolean answered = false;
    private CountDownTimer countDownTimer;
    private boolean ended = false;

    private String deviceId;
    private String callReason;

    private long leftMinutesOnStart;
    private long lastLeftMinutes;

    public CallPresenter(CallView view) {
        this.view = view;
        this.firebaseManager = FirebaseManager.getInstance();
        this.networkManager = new NetworkManager(view.getContext());
        DataSnapshot dataSnapshot = firebaseManager.getCurrentSnapshot();
        if (dataSnapshot == null) {
            view.navigateBack();
            return;
        }

        this.deviceId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseTrialMinutes trialMinutes = dataSnapshot.getValue(FirebaseTrialMinutes.class);
        trialMinutes.setKey(dataSnapshot.getKey());
        leftMinutesOnStart = trialMinutes.getMinutes() + trialMinutes.getExtra();
        lastLeftMinutes = leftMinutesOnStart;
    }

    @Override
    public void onCreate(Bundle extras) {
        callReason = extras.getString(REASON_EXTRA);
        view.toggleEndCallButtonVisibility(false);
        view.updateLeftTime(new HumanTime(leftMinutesOnStart).getTime());
        view.updateCurrentCallDuration("00:00");
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
        if (ended) {
            return;
        }
        ended = true;
        if (!answered && call != null) {
            firebaseManager.cancelCall(call);
        }
        if (session != null) {
            session.disconnect();
            session.setSessionListener(null);
            if (subscriber != null) {
                subscriber.setSubscriberListener(null);
                session.unsubscribe(subscriber);
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
        publisher.setPublisherListener(CallPresenter.this);

        view.showSelfView(publisher.getView());

        session.publish(publisher);

        call = firebaseManager.makeCall(session.getSessionId(), getNewCallName(), callReason);

    }


    @Override
    public void onDisconnected(Session session) {
        endCall();
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null) {
            subscriber = new Subscriber.Builder(view.getContext(), stream).build();
            session.subscribe(subscriber);
            view.showInterpreterView(subscriber.getView());
            connectionEstablished();
        }
    }

    private static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap getBitmapFromDrawable(int drawableId) {
        Bitmap bitmap;
        Drawable drawable = view.getContext().getResources().getDrawable(drawableId);
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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
                firebaseManager.reduceMinutes(deviceId, lastLeftMinutes - millisUntilFinished);
                lastLeftMinutes = millisUntilFinished;
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
        Log.d(TAG, opentokError.getMessage());
        if (ended) {
            return;
        }
        view.hideIndicator();
        view.showError(opentokError.getMessage());
    }

    private String getNewCallName() {
        String date = (new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())).format(new Date());
        String reason = this.callReason;
        if (reason == null) {
            reason = view.getContext().getString(R.string.call_reason_other);
        }

        return String.format(
                Locale.getDefault(),
                "%s - %s - %s - %s",
                new UserManager(view.getContext()).getUserName(),
                reason,
                DeviceName.getDeviceName(),
                date
        );
    }
}
