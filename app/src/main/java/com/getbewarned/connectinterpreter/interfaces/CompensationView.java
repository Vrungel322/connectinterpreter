package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

import javax.annotation.Nullable;

public interface CompensationView {
    Context getContext();

    void showError(String message, @Nullable Throwable throwable);

    void goBack();
}
