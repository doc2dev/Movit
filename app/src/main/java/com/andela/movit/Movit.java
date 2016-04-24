package com.andela.movit;

import android.app.Application;
import android.support.test.espresso.IdlingResource;

import com.andela.movit.async.TrackingService;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

public class Movit extends Application implements IdlingResource {

    protected boolean isIdle = false;

    protected IdlingResource.ResourceCallback resourceCallback;

    private TrackingService trackingService;

    private static Movit app;

    @Override
    public void onCreate() {
        super.onCreate();
        Movit.app = (Movit)getApplicationContext();
        Utility.launchService(this, TrackingService.class);
    }

    public TrackingService getTrackingService() {
        return trackingService;
    }

    public void setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    public static Movit getApp() {
        return app;
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

    @Override
    public void onTerminate() {
        if (trackingService != null && !trackingService.isTracking()) {
            Utility.stopService(this, TrackingService.class);
        }
        super.onTerminate();
    }

    public Movit() {
        super();
    }
}
