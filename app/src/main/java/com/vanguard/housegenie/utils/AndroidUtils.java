package com.vanguard.housegenie.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AndroidUtils {

    public static final String countryPreference = "Country";

    public static void saveUserPreference(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUserPreference(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }
}
