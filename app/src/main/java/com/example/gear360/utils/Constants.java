package com.example.gear360.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Constants {
    public static final String CAMERA_IP_ADDRESS = "192.168.43.1";
    public static final String OSC_BASE_URL = "http://" + CAMERA_IP_ADDRESS + ":80/";

    private static final String PREF_NAME = "Gear360Prefs";
    private static final String KEY_PASSWORD = "camera_password";

    public static void savePassword(Context context, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }

    public static String getSavedPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, null);
    }
}