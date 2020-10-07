package com.getbewarned.connectinterpreter.interfaces;

import android.content.Context;

import javax.annotation.Nullable;

public interface NameInputView {

    Context getContext();

    void showError(String message, @Nullable Throwable throwable);

    void navigateToApp();

}
