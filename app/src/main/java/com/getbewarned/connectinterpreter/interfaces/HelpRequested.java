package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;

/**
 * Created by artycake on 3/5/18.
 */

public interface HelpRequested {
    void onHelpRequested();

    void onErrorReceived(Error error);
}
