package com.andela.movit;

import android.app.Application;

public class Movit extends Application{

    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        Movit.app = (Movit)getApplicationContext();
    }

    private static Movit app;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public static Movit getApp() {
        return app;
    }
}
