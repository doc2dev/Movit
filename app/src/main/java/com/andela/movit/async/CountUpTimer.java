package com.andela.movit.async;

import java.util.Timer;
import java.util.TimerTask;

public class CountUpTimer {

    private TimerTickListener listener;

    private long baseTime;

    private Timer timer;

    private boolean active;

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

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
