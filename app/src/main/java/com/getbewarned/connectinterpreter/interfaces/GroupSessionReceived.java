package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.GroupSessionResponse;

public interface GroupSessionReceived {

    void onDataReceived(GroupSessionResponse response);

    void onErrorReceived(Error error);
}
