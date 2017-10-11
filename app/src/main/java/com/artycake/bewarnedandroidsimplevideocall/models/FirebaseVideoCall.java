package com.artycake.bewarnedandroidsimplevideocall.models;

import com.google.firebase.database.Exclude;

/**
 * Created by artycake on 8/31/17.
 */

public class FirebaseVideoCall {

    public static enum Status {
        NEW("waiting"),
        CANCELED("missed"),
        ANSWERED("answered"),
        ENDED("ended");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @Exclude
    private String key;
    private String callName;
    private String callerId;
    private String interpreterId;
    private long timestampStart;
    private long timestampAnswer;
    private long timestampEnd;
    private String status;
    private String sessionId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getInterpreterId() {
        return interpreterId;
    }

    public void setInterpreterId(String interpreterId) {
        this.interpreterId = interpreterId;
    }

    public long getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(long timestampStart) {
        this.timestampStart = timestampStart;
    }

    public long getTimestampAnswer() {
        return timestampAnswer;
    }

    public void setTimestampAnswer(long timestampAnswer) {
        this.timestampAnswer = timestampAnswer;
    }

    public long getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(long timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
