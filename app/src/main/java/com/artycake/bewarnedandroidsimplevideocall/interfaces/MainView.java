package com.artycake.bewarnedandroidsimplevideocall.interfaces;

import android.content.Context;

/**
 * Created by artycake on 10/11/17.
 */

public interface MainView {

    void showLeftTime(String leftTime);

    void toggleCallButtonEnabled(boolean enabled);

    Context getContext();

    void navigateToCall();
}
