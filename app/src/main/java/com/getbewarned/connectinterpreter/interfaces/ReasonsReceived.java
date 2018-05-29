package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ReasonsResponse;

public interface ReasonsReceived {
    void onReasonsReceived(ReasonsResponse response);

    void onErrorReceived(Error error);
}
