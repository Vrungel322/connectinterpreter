package com.artycake.bewarnedandroidsimplevideocall.presenters;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.artycake.bewarnedandroidsimplevideocall.interfaces.AuthStateChanged;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.FirebaseValueListener;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.MainView;
import com.artycake.bewarnedandroidsimplevideocall.interfaces.Presenter;
import com.artycake.bewarnedandroidsimplevideocall.managers.FirebaseManager;
import com.artycake.bewarnedandroidsimplevideocall.managers.UserManager;
import com.artycake.bewarnedandroidsimplevideocall.models.FirebaseTrialMinutes;
import com.artycake.bewarnedandroidsimplevideocall.models.HumanTime;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by artycake on 10/11/17.
 */

public class MainPresenter implements Presenter {
    private MainView view;
    private FirebaseManager firebaseManager;
    private UserManager userManager;
    private String deviceId;

    private FirebaseTrialMinutes trialMinutes;

    private long maxMinutesPerDay = 10 * 60 * 1000; // in miliseconds

    public MainPresenter(MainView view) {
        this.view = view;
        this.deviceId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        this.userManager = new UserManager(view.getContext());
        this.firebaseManager = FirebaseManager.getInstance();
        this.firebaseManager.setAuthStateChanged(this.authStateChanged);

    }

    private AuthStateChanged authStateChanged = new AuthStateChanged() {
        @Override
        public void onAuthStateChanged(String userId) {
            userManager.updateUserId(userId);
            if (userId != null) {
                firebaseManager.checkLeftMinutes(deviceId, firebaseValueListener);
            }
        }
    };

    private FirebaseValueListener firebaseValueListener = new FirebaseValueListener() {
        @Override
        public void onDataChanged(DataSnapshot dataSnapshot) {
            FirebaseTrialMinutes trialMinutes = dataSnapshot.getValue(FirebaseTrialMinutes.class);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if (trialMinutes == null) {
                trialMinutes = new FirebaseTrialMinutes();
                trialMinutes.setDeviceId(deviceId);
                trialMinutes.setMinutes(maxMinutesPerDay);
                trialMinutes.setDate(cal.getTimeInMillis());
                firebaseManager.updateLeftMinutes(trialMinutes);
                return;
            }

            trialMinutes.setKey(dataSnapshot.getKey());
            if (trialMinutes.getDate() < cal.getTimeInMillis()) {
                trialMinutes.setMinutes(maxMinutesPerDay);
                trialMinutes.setDate(cal.getTimeInMillis());
                firebaseManager.updateLeftMinutes(trialMinutes);
                return;
            }

            processTrial(trialMinutes);
        }
    };

    private void processTrial(FirebaseTrialMinutes trialMinutes) {
        this.trialMinutes = trialMinutes;
        HumanTime humanTime = new HumanTime(trialMinutes.getMinutes());
        if (humanTime.getMinutes() > 0 || humanTime.getSeconds() > 0) {
            view.toggleCallButtonEnabled(true);
        }
        view.showLeftTime(humanTime.getTime());
    }

    @Override
    public void onCreate(Bundle extras) {
        view.toggleCallButtonEnabled(false);
        view.showLeftTime("00:00");
        firebaseManager.login();
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDestroy() {
    }

    public void onStartCallPressed() {
        view.navigateToCall();
    }
}
