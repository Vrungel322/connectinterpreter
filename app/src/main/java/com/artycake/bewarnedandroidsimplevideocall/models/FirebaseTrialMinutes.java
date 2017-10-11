package com.artycake.bewarnedandroidsimplevideocall.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

/**
 * Created by artycake on 10/11/17.
 */

public class FirebaseTrialMinutes {

    @Exclude
    private String key;
    private long minutes;
    private String deviceId;
    private long date;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
