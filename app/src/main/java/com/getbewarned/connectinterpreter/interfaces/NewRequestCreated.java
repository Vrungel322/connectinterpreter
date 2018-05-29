package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.NewRequestResponse;

public interface NewRequestCreated {
    void onRequestCreated(NewRequestResponse response);

    void onErrorReceived(Error error);
}
