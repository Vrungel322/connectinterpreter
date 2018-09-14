package com.getbewarned.connectinterpreter.models;

import android.content.Context;

import com.getbewarned.connectinterpreter.R;

import java.util.Locale;

/**
 * Created by artycake on 10/11/17.
 */

public class HumanTime {
    private int minutes;
    private int seconds;
    private String time;
    private String easyTime;

    public HumanTime(Context context, long milliseconds) {
        minutes = (int) ((milliseconds / 1000) / 60);
        seconds = (int) ((milliseconds - minutes * 1000 * 60) / 1000);
        time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        easyTime = context.getResources().getQuantityString(R.plurals.minutes, minutes, minutes) + " " +
                context.getResources().getQuantityString(R.plurals.seconds, seconds, seconds);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getTime() {
        return time;
    }

    public String getEasyTime() {
        return easyTime;
    }
}
