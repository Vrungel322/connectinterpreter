package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ProfileResponse;

public interface ProfileReceived {
    void onReceived(ProfileResponse response);

    void onErrorReceived(Error error);
}
