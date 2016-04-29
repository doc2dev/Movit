/**
 * This class co-ordinates all operations involved in tracking. It exposes methods for starting
 * and stopping tracking.
 * */

package com.andela.movit.background;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.andela.movit.Movit;
import com.andela.movit.config.Constants;
import com.andela.movit.location.IncomingStringCallback;
import com.andela.movit.location.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.PreferenceHelper;
import com.andela.movit.utilities.TrackingHelper;
import com.andela.movit.utilities.FrameworkUtils;

public class ServiceHelper {

    private Context context;

    private Movement movement;

    protected String currentActivity;

    protected String previousActivity;

    private TrackingHelper trackingHelper;

    private CountUpTimer counter;

    public ServiceHelper(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        initMovement();
        initCounter();
        initActivity();
        prepareHelper();
    }

    private void initMovement() {
        movement = Movit.getApp().getInitialLocation();
        if (movement != null) {
            currentActivity = movement.getActivityName();
        }
    }

    private void initCounter() {
        counter = new CountUpTimer();
        counter.setListener(getTickListener());
    }

    private void initActivity() {
        previousActivity = "Unknown";
        currentActivity = "Unknown";
    }

    /**
     * Starts tracking.
     * */

    public void startTracking() {
        prepareHelper();
        restartCounter();
        trackingHelper.startTracking();
        Movit.getApp().setTracking(true);
    }

    /**
     * Stops tracking.
     * */

    public void stopTracking() {
        trackingHelper.stopTracking();
        counter.stop();
        logMovement(currentActivity);
        Movit.getApp().setTracking(false);
    }

    private void logMovement(String activityName) {
        long elapsedTime = counter.getElapsedTime();
        if (trackingHelper.hasTimeElapsed(elapsedTime)) {
            trackingHelper.logActivity(activityName, elapsedTime);
        }
    }

    private void restartCounter() {
        counter.stop();
        counter.reset();
        counter.start();
    }

    private TimerTickListener getTickListener() {
        return new TimerTickListener() {
            @Override
            public void onTick(long elapsedTime) {
                broadcastLocation(movement);
            }
        };
    }

    private void prepareHelper() {
        trackingHelper = new TrackingHelper(Movit.getApp());
        trackingHelper.setMovement(movement);
        trackingHelper.setLocationCallback(getLocationCallback());
        trackingHelper.setActivityCallback(getActivityCallback());
        PreferenceHelper preferenceHelper = new PreferenceHelper(Movit.getApp());
        trackingHelper.setDurationBeforeLogging(preferenceHelper.getTimeBeforeLogging());
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationDetected(Movement mv) {
                handleLocationChange(mv);
            }
        };
    }

    private void handleLocationChange(Movement mv) {
        movement = mv;
        trackingHelper.setMovement(mv);
        broadcastLocation(mv);
    }

    private void broadcastLocation(Movement movement) {
        if (movement != null) {
            Intent intent = new Intent(Constants.LOCATION.getValue());
            intent = FrameworkUtils.putMovementInIntent(movement, intent);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private IncomingStringCallback getActivityCallback() {
        return new IncomingStringCallback() {
            @Override
            public void onStringArrive(String activityName) {
                if (trackingHelper.hasActivityChanged(activityName)) {
                    handleActivityChange(activityName);
                }
                broadcastActivityStatement();
            }
        };
    }

    private void handleActivityChange(String activityName) {
        previousActivity = currentActivity;
        currentActivity = activityName;
        trackingHelper.setCurrentActivity(currentActivity);
        logMovement(previousActivity);
        restartCounter();
    }

    private void broadcastActivityStatement() {
        Intent intent = new Intent(Constants.STATEMENT.getValue());
        intent.putExtra(
                Constants.STATEMENT.getValue(),
                trackingHelper.getActivityStatement(currentActivity));
        FrameworkUtils.broadcastIntent(context, intent);
    }

}
