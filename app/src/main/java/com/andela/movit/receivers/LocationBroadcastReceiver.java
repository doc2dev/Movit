/**
 * This class defines a broadcast receiver that reacts to broadcasts of the current location.
 * */

package com.andela.movit.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.andela.movit.location.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.FrameworkUtils;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    private LocationCallback locationCallback;

    /**
     * Constructs a {@code LocationBroadcastReceiver} object.
     * @param locationCallback the callback that will be invoked whenever a new location broadcast
     * is received.
     * */

    public LocationBroadcastReceiver(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Movement movement = FrameworkUtils.getMovementFromBundle(intent.getExtras());
        locationCallback.onLocationDetected(movement);
    }
}
