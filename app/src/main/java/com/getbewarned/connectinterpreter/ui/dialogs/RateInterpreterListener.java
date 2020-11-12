package com.getbewarned.connectinterpreter.ui.dialogs;

public interface RateInterpreterListener {
    void onRateDone(int rating, String feedback);

    void rateSkipped();
}
