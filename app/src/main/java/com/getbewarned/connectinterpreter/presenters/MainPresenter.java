package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.AuthStateChanged;
import com.getbewarned.connectinterpreter.interfaces.FirebaseValueListener;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.managers.FirebaseManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.FirebaseTrialMinutes;
import com.getbewarned.connectinterpreter.models.HumanTime;
import com.google.firebase.database.DataSnapshot;
import com.jaredrummler.android.device.DeviceName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    private boolean hasAccess = false;

    public MainPresenter(MainView view) {
        this.view = view;
        this.deviceId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        this.userManager = new UserManager(view.getContext());
        this.firebaseManager = FirebaseManager.getInstance();
    }

    private AuthStateChanged authStateChanged = new AuthStateChanged() {
        @Override
        public void onAuthStateChanged(String userId) {
            userManager.updateUserId(userId);
            if (userId != null) {
                firebaseManager.checkLeftMinutes(deviceId, firebaseValueListener);
            } else {
                view.showError(view.getContext().getString(R.string.internet_failure));
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
                trialMinutes.setDeviceModel(DeviceName.getDeviceName());
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
            if (trialMinutes.getName() == null || trialMinutes.getName().isEmpty()) {
                nameChanged(userManager.getUserName());
            } else {
                userManager.updateUserName(trialMinutes.getName());
                view.updateUserName(trialMinutes.getName());
            }

            processTrial(trialMinutes);
        }
    };

    private void processTrial(FirebaseTrialMinutes trialMinutes) {
        this.trialMinutes = trialMinutes;
        HumanTime humanTime = new HumanTime(trialMinutes.getMinutes() + trialMinutes.getExtra());
        view.toggleCallAvailability(humanTime.getMinutes() > 0 || humanTime.getSeconds() > 0);
        view.showLeftTime(humanTime.getTime());
    }

    @Override
    public void onCreate(Bundle extras) {
        view.showChecking();
        view.showLeftTime("00:00");
        String name = userManager.getUserName();
        if (name.isEmpty()) {
            view.askForName(null);
        } else {
            view.updateUserName(name);
            view.requestPermissions();
        }
    }

    public void onPermissionsGranted() {
        this.firebaseManager.setAuthStateChanged(this.authStateChanged);
        firebaseManager.login();
    }

    public void onPause() {
    }

    public void onResume() {
        view.showChecking();
        firebaseManager.checkLeftMinutes(deviceId, firebaseValueListener);
    }

    public void onDestroy() {
    }

    public void onStartCallPressed() {

        if (isWorkTime()) {
            view.askForReason();
        } else {
            view.showError(view.getContext().getString(R.string.not_work_time));
            String date = (new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())).format(new Date());
            String reason = "Звонок в нерабочее время";
            String callName = String.format(
                    Locale.getDefault(),
                    "%s - %s - %s - %s",
                    userManager.getUserName(),
                    reason,
                    DeviceName.getDeviceName(),
                    date
            );
            firebaseManager.makeMissedCall(callName, reason);
        }
    }

    public void reasonSelected(String reason) {
        view.navigateToCallFor(reason);
    }

    private boolean isWorkTime() {
        Calendar nowDate = Calendar.getInstance();
        Calendar fromDate = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
        fromDate.set(nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DATE), 9, 0);
        Calendar tillDate = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
        tillDate.set(nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DATE), 18, 0);

        return nowDate.getTime().after(fromDate.getTime()) && nowDate.getTime().before(tillDate.getTime());
    }

    public void nameChanged(String name) {
        userManager.updateUserName(name);
        view.updateUserName(name);
        if (!this.hasAccess) {
            view.requestPermissions();
        }
    }

    public void userNamePressed() {
        view.askForName(userManager.getUserName());
    }
}
