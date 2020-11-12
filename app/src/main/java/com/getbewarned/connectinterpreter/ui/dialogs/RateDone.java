package com.getbewarned.connectinterpreter.ui.dialogs;

public interface RateDone {
    void onRateDone(int rating, String feedback);

    void rateSkipped();
}
