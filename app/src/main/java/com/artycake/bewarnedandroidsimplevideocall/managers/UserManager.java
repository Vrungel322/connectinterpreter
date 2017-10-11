package com.artycake.bewarnedandroidsimplevideocall.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

/**
 * Created by artycake on 10/11/17.
 */

public class UserManager {

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String TIME_LEFT = "user_name";

    private String userId;
    private String userName;

    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "");
        userName = sharedPreferences.getString(USER_NAME, "");
    }

    public void updateUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
        this.userId = userId;
    }

    public void updateUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
        this.userName = userName;
    }
}
