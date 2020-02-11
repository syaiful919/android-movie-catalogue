package com.syaiful.moviecatalogue.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String PREFS_NAME = "user_pref";
    private static final String REMINDER = "isReminderActive";
    private static final String RELEASE = "isReleaseActive";
    private final SharedPreferences preferences;

    public Pref(Context context){
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setReminderpPref(boolean val){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(REMINDER, val);
        editor.apply();
    }

    public void setReleasePref(boolean val){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RELEASE, val);
        editor.apply();
    }

    public boolean getReminderPref(){
        boolean val = preferences.getBoolean(REMINDER, false);
        return val;
    }

    public boolean getReleasePref(){
        boolean val = preferences.getBoolean(RELEASE, false);
        return val;
    }
}
