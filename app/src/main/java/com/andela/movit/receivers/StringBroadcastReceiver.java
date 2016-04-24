package com.andela.movit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andela.movit.listeners.IncomingStringCallback;

public class StringBroadcastReceiver extends BroadcastReceiver {

    private IncomingStringCallback callback;

    private String actionName;

    public StringBroadcastReceiver(String actionName) {
        this.actionName = actionName;
    }

    public void setIncomingStringCallback(IncomingStringCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String expectedString = intent.getStringExtra(actionName);
        callback.onStringArrive(expectedString);
    }
}
