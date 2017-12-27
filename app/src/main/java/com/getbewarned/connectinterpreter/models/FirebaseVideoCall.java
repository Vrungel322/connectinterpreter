package com.getbewarned.connectinterpreter.models;

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
    private Object timestampStartUTC;
    private Object timestampAnswerUTC;
    private Object timestampEndUTC;
    private String reason;

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

    public Object getTimestampStartUTC() {
        return timestampStartUTC;
    }

    public void setTimestampStartUTC(Object timestampStartUTC) {
        this.timestampStartUTC = timestampStartUTC;
    }

    public Object getTimestampAnswerUTC() {
        return timestampAnswerUTC;
    }

    public void setTimestampAnswerUTC(Object timestampAnswerUTC) {
        this.timestampAnswerUTC = timestampAnswerUTC;
    }

    public Object getTimestampEndUTC() {
        return timestampEndUTC;
    }

    public void setTimestampEndUTC(Object timestampEndUTC) {
        this.timestampEndUTC = timestampEndUTC;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
