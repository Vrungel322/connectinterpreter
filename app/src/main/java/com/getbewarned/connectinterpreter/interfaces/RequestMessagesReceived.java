package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.MessagesResponse;

/**
 * Created by artycake on 1/17/18.
 */

public interface RequestMessagesReceived {
    void onMessagesReceived(MessagesResponse response);

    void onErrorReceived(Error error);
}
