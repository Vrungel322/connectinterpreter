package com.getbewarned.connectinterpreter.interfaces;


import android.content.Intent;
import android.graphics.Bitmap;

public interface RequestFileSelection {

    void showChoiceSheet();

    Bitmap getImageFromActivityResult(int requestCode, int resultCode, Intent data);
}
