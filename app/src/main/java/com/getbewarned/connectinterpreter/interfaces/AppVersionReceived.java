package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.AppVersionResponse;

/**
 * Created by artycake on 1/24/18.
 */

public interface AppVersionReceived {
    void onAppVersionReceived(AppVersionResponse response);

    void onErrorReceived(Error error);
}
