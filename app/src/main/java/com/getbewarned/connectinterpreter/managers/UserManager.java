package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.getbewarned.connectinterpreter.models.UtogAsk;

import java.util.Date;

/**
 * Created by artycake on 10/11/17.
 */

public class UserManager {

    public static final String FIRST_TIME = "first_time";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_REGION = "user_region";
    public static final String USER_SECONDS = "user_seconds";
    public static final String USER_UNLIM = "user_unlim";
    public static final String USER_ACTIVE_TILL = "user_active_till";
    public static final String USER_UTOG = "user_utog";
    public static final String USER_UTOG_AVAILABLE = "user_utog_available";
    public static final String USER_UKRAINIAN = "user_ukrainian";
    public static final String UTOG_ASK = "utog_ask";
    public static final String UTOG_ASK_DATE = "utog_ask_date";

    private static final String LAST_APP_VERSION = "last_app_version";


    private static final String LAST_CALL_SESSION_ID = "last_call_session_id";


    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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

    public void updateUserPhone(String userPhone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PHONE, userPhone);
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

    public void updateLastAppVersion(int version) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LAST_APP_VERSION, version);
        editor.apply();
    }

    public void updateUserUtog(boolean isUtog) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_UTOG, isUtog);
        editor.apply();
    }

    public void updateUtogCallAvailable(boolean isAvailable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_UTOG_AVAILABLE, isAvailable);
        editor.apply();
    }

    public void updateUserUkrainian(boolean isUkrainian) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_UKRAINIAN, isUkrainian);
        editor.apply();
    }

    public void updateUtogAsk(UtogAsk utogAsk) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UTOG_ASK, String.valueOf(utogAsk.getStatus()));
        if (utogAsk.getStatus() == UtogAsk.STATUS.LATER) {
            editor.putLong(UTOG_ASK_DATE, utogAsk.getDate().getTime());
        }
        editor.apply();
    }

    public void updateLastCallSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_CALL_SESSION_ID, sessionId);
        editor.apply();
    }

    public void updateFirstTime(boolean firstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, firstTime);
        editor.apply();
    }
    public void updateUserRegion(String region) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_REGION, region);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, "");
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

    public int getLastAppVersion() {
        return sharedPreferences.getInt(LAST_APP_VERSION, 0);
    }

    public boolean getUserUtog() {
        return sharedPreferences.getBoolean(USER_UTOG, false);
    }

    public boolean getUserUkrainian() {
        return sharedPreferences.getString(USER_REGION, "").equals("ua");
    }

    public UtogAsk getUtogAsk() {
        String ask = sharedPreferences.getString(UTOG_ASK, String.valueOf(UtogAsk.STATUS.SHOUD_ASK));
        UtogAsk.STATUS status = UtogAsk.STATUS.toStatus(ask);
        if (status == UtogAsk.STATUS.LATER) {
            Date date = new Date(sharedPreferences.getLong(UTOG_ASK_DATE, new Date().getTime()));
            return new UtogAsk(status, date);
        }
        return new UtogAsk(status, null);
    }

    public String getLastCallSessionId() {
        return sharedPreferences.getString(LAST_CALL_SESSION_ID, null);
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME, false);
    }

    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public boolean isUtogAvailable() {
        return sharedPreferences.getBoolean(USER_UTOG_AVAILABLE, false);
    }


}
