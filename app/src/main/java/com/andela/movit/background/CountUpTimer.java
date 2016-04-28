/**
 * This class implements a simple count-up timer, with operations for starting, stopping and
 * resetting a timer, and for setting a callback to be invoked every time the timer ticks.
 * */

package com.andela.movit.background;

import java.util.Timer;
import java.util.TimerTask;

public class CountUpTimer {

    private TimerTickListener listener;

    private long baseTime = 0;

    private Timer timer;

    private boolean active;

    /**
     * Sets the callback to be invoked when the timer ticks.
     * @param listener The callback to be invoked.
     * */

    public void setListener(TimerTickListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the timer.
     * */

    public void start() {
        if (!active) {
            timer = new Timer("CountUpTimer");
            timer.scheduleAtFixedRate(getTask(), 0, 1000);
            active = true;
        }
    }

    /**
     * Stops the timer.
     * */

    public void stop() {
        if (active) {
            timer.cancel();
            active = false;
        }
    }

    /**
     * Resets the timer.
     * */

    public void reset() {
        baseTime = 0;
    }

    /**
     * Returns the value (in milliseconds) that has elapsed since the timer was started.
     * */

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
