package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by artycake on 10/11/17.
 */

public class UserManager {

    private static final String USER_TOKEN = "user_token";
    private static final String USER_NAME = "user_name";
    private static final String USER_SECONDS = "user_seconds";
    private static final String USER_UNLIM = "user_unlim";
    private static final String USER_ACTIVE_TILL = "user_active_till";

    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void updateUserToken(String userToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, userToken);
        editor.apply();
        ;
    }

    public void updateUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public void updateUserSeconds(long seconds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_SECONDS, seconds);
        editor.apply();
    }

    public void updateUserUnlim(boolean unlim) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_UNLIM, unlim);
        editor.apply();
    }

    public void updateUserActiveTill(long timestamp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(USER_ACTIVE_TILL, timestamp);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public long getUserSeconds() {
        return sharedPreferences.getLong(USER_SECONDS, 0);
    }

    public boolean getUserUnlim() {
        return sharedPreferences.getBoolean(USER_UNLIM, false);
    }

    public long getUserActiveTill() {
        return sharedPreferences.getLong(USER_ACTIVE_TILL, 0);
    }
}
