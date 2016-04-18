package com.andela.movit.test_support;

import android.support.test.espresso.IdlingResource;

public class TestableAsync implements IdlingResource {

    protected boolean isIdle = false;

    protected ResourceCallback resourceCallback;

    @Override
    public String getName() {
        return "MovementHelper";
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
