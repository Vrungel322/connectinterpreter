package com.getbewarned.connectinterpreter.models;


import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Created by artycake on 2/1/18.
 */

public class UtogAsk {

    public enum STATUS {
        NEVER,
        LATER,
        SHOUD_ASK;

        public static STATUS toStatus (String status) {
            try {
                return valueOf(status);
            } catch (Exception ex) {
                return SHOUD_ASK;
            }
        }

    }

    STATUS status;
    Date date;

    public UtogAsk(STATUS status, @Nullable Date date) {
        this.status = status;
        this.date = date;
    }

    public STATUS getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }
}
