package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.NewMessageResponse;

public interface NewRequestMessageCreated {
    void onMessageCreated(NewMessageResponse response);

    void onErrorReceived(Error error);
}
