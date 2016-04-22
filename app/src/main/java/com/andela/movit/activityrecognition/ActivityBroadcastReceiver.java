package com.andela.movit.activityrecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andela.movit.config.Constants;
import com.andela.movit.listeners.ActivityCallback;

public class ActivityBroadcastReceiver extends BroadcastReceiver {

    public void setActivityCallback(ActivityCallback callback) {
        this.callback = callback;
    }

    private ActivityCallback callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        String activityName = intent.getStringExtra(Constants.ACTIVITY_NAME.getValue());
        callback.onActivityDetected(activityName);
    }
}
