package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

/**
 * Created by artycake on 2/2/18.
 */

public interface UtogView {

    void navigateBack();

    Context getContext();

    void showError(String message);

    void showSuccess(String message);

}
