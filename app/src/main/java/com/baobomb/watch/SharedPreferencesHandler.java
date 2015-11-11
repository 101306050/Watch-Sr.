package com.baobomb.watch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by baobomb on 2015/11/11.
 */
public class SharedPreferencesHandler {
    SharedPreferences sharedPreferences;

    public SharedPreferencesHandler(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void keepWatchUserName(String id, String name) {
        sharedPreferences.edit().putString(id, name).commit();
    }

    public String getWatchName(String id) {
        return sharedPreferences.getString(id, id);
    }
}
