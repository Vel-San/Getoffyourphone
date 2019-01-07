package com.nephi.getoffyourphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultSettings {
    private static SharedPreferences sharedPreferences;

    private static void getSharedPreferencesInstance(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //Get CheckBox1 value
    public static boolean getCb1(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("cb1", false);
        //return (key, defaultValue);
    }

    //Get CheckBox2 value
    public static boolean getCb2(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("cb2", false);
    }

    //Get Switch1 value
    public static boolean getTheme(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("theme", false);
    }

}
