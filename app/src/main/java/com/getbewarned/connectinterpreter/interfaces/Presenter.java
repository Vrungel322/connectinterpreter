package com.getbewarned.connectinterpreter.interfaces;

import android.os.Bundle;

/**
 * Created by artycake on 10/11/17.
 */

public interface Presenter {
    void onCreate(Bundle extras);

    void onPause();

    void onResume();

    void onDestroy();

}
