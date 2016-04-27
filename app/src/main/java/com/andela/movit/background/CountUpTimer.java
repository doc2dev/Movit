package com.andela.movit.background;

import java.util.Timer;
import java.util.TimerTask;

public class CountUpTimer {

    private TimerTickListener listener;

    private long baseTime = 0;

    private Timer timer;

    private boolean active;

    public void setListener(TimerTickListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (!active) {
            timer = new Timer("CountUpTimer");
            timer.scheduleAtFixedRate(getTask(), 0, 1000);
            active = true;
        }
    }

    public void stop() {
        if (active) {
            timer.cancel();
            active = false;
        }
    }

    public void reset() {
        baseTime = 0;
    }

    public long getElapsedTime() {
        return baseTime;
    }

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                baseTime += 1000;
                listener.onTick(baseTime);
            }
        };
    }
}
