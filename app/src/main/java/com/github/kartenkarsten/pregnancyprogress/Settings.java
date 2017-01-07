package com.github.kartenkarsten.pregnancyprogress;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Settings {
    private SharedPreferences _sharedPreferences;

    public Settings(Context context) {
        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        return _sharedPreferences.getString(key, null);
    }

    public void put(String key, String value) {
        Editor e = _sharedPreferences.edit();
        e.putString(key, value);
        e.commit();
    }
}
