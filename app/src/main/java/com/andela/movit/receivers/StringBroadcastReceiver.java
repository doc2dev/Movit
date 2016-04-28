/**
 * This class defines a broadcast receiver that reacts to broadcasts of a specified string value.
 * */

package com.andela.movit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andela.movit.location.IncomingStringCallback;

public class StringBroadcastReceiver extends BroadcastReceiver {

    private IncomingStringCallback callback;

    private String actionName;

    /**
     * Constructs a receiver for the specified string value
     * @param actionName the name that will be used to filter for the expected string broadcast.
     * */

    public StringBroadcastReceiver(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Sets the callback that will be invoked whenever a broadcast is received.
     * */

    public void setIncomingStringCallback(IncomingStringCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String expectedString = intent.getStringExtra(actionName);
        callback.onStringArrive(expectedString);
    }
}
