package com.andela.movit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    private LocationCallback locationCallback;

    public LocationBroadcastReceiver(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Movement movement = Utility.getMovementFromBundle(intent.getExtras());
        locationCallback.onLocationDetected(movement);
    }
}
