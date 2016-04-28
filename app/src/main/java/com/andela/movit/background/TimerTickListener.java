/**
 * This interface provides a callback to be invoked whenever a {@code CountUpTimer} timer ticks.
 * */

package com.andela.movit.background;

public interface TimerTickListener {

    /**
     * Called whenever the timer ticks.
     * @param elapsedTime the time (in milliseconds) since the timer was started.
     * */

    void onTick(long elapsedTime);
}
