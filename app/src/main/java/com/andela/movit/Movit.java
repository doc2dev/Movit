package com.andela.movit;

import android.app.Application;
import android.support.test.espresso.IdlingResource;

import com.andela.movit.async.TrackingService;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

public class Movit extends Application implements IdlingResource {

    protected boolean isIdle = false;

    protected IdlingResource.ResourceCallback resourceCallback;

    private boolean isTrackingServiceRunning;

    private Movement movement;

    private boolean trackerActive;

    public boolean isTrackerActive() {
        return trackerActive;
    }

    public void setTrackerActive(boolean trackerActive) {
        this.trackerActive = trackerActive;
    }

    private static Movit app;

    @Override
    public void onCreate() {
        super.onCreate();
        Movit.app = (Movit)getApplicationContext();
    }

    public static Movit getApp() {
        return app;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    @Override
    public String getName() {
        return "App";
    }

    @Override
    public boolean isIdleNow() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
        if (isIdle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public boolean isTrackingServiceRunning() {
        return isTrackingServiceRunning;
    }

    public void setTrackingServiceRunning(boolean trackingServiceRunning) {
        isTrackingServiceRunning = trackingServiceRunning;
    }

    @Override
    public void onTerminate() {
        Utility.stopService(this, TrackingService.class);
        super.onTerminate();
    }
}
