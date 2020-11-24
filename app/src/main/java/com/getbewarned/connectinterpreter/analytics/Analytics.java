package com.getbewarned.connectinterpreter.analytics;

public class Analytics {
    private static final Analytics ourInstance = new Analytics();

    public static Analytics getInstance() {
        return ourInstance;
    }

    private Analytics() {
    }

    public void trackEvent(String eventName) {

    }

}
