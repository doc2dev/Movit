package com.andela.movit;

import android.app.Application;
import android.support.test.espresso.IdlingResource;

public class Movit extends Application implements IdlingResource {

    protected boolean isIdle = false;

    protected IdlingResource.ResourceCallback resourceCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        Movit.app = (Movit)getApplicationContext();
    }

    private static Movit app;

    public static Movit getApp() {
        return app;
    }

    @Override
    public String getName() {
        return "LocationHelper";
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
}
