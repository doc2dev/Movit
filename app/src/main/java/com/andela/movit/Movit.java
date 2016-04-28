package com.andela.movit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.test.espresso.IdlingResource;

import com.andela.movit.background.TrackingService;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

public class Movit extends Application implements IdlingResource {

    protected boolean isIdle = false;

    private boolean isAppLaunched;

    private boolean isTracking;

    protected IdlingResource.ResourceCallback resourceCallback;

    private Movement initialLocation;

    private static Movit app;

    @Override
    public void onCreate() {
        super.onCreate();
        Movit.app = (Movit)getApplicationContext();
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

    public boolean isAppLaunched() {
        return isAppLaunched;
    }

    public void setAppLaunched(boolean appLaunched) {
        isAppLaunched = appLaunched;
    }

    public Movement getInitialLocation() {
        return initialLocation;
    }

    public void setInitialLocation(Movement initialLocation) {
        this.initialLocation = initialLocation;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    @Override
    public void onTerminate() {
        Utility.stopService(this, TrackingService.class);
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
