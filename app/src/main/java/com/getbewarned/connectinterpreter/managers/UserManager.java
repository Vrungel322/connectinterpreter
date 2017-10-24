package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by artycake on 10/11/17.
 */

public class UserManager {

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";

    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void updateUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();;
    }

    public void updateUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }
}
