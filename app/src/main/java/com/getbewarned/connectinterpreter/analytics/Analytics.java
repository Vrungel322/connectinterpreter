package com.getbewarned.connectinterpreter.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    private static final Analytics ourInstance = new Analytics();
    private FirebaseAnalytics mFirebaseAnalytics;

    public static Analytics getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    private Analytics() {
    }

    public void trackEvent(String eventName) {
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics.logEvent(eventName, null);
        }
    }

}
