package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.NameResponse;

/**
 * Created by artycake on 10/11/17.
 */

public interface NameChanged {
    void onNameChanged(NameResponse response);

    void onErrorReceived(Error error);
}
