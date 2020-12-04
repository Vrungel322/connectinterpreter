package com.getbewarned.connectinterpreter;

import android.app.Application;

import com.getbewarned.connectinterpreter.analytics.Analytics;

import java.io.BufferedInputStream;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        if (BuildConfig.BUILD_TYPE.equals("release")) {
            Analytics.getInstance().init(this);
        }
    }

}
