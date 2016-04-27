package com.andela.movit.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.andela.movit.config.Constants;

public class PreferenceHelper {

    private Context context;

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    public PreferenceHelper(Context context) {
        this.context = context;
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public long getTimeBeforeLogging() {
        int minutes = preferences.getInt(Constants.TIME_BEFORE_LOGGING.getValue(), 1);
        return minutes * 60000;
    }

    public void setTimeBeforeLogging(int timeBeforeLogging) {
        editor.putInt(Constants.TIME_BEFORE_LOGGING.getValue(), timeBeforeLogging);
        editor.commit();
    }
}
