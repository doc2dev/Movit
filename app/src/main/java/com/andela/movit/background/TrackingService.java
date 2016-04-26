package com.andela.movit.background;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.andela.movit.Movit;
import com.andela.movit.config.Constants;
import com.andela.movit.listeners.IncomingStringCallback;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.PreferenceHelper;
import com.andela.movit.utilities.TrackingHelper;
import com.andela.movit.utilities.Utility;

public class TrackingService extends InfiniteService {

    private Movement movement;

    private String currentActivity = "Unknown";

    private TrackingHelper trackingHelper;

    private CountUpTimer counter;

    private long timeElapsed;

    public TrackingService() {
        super("TrackingService");
        init();
    }

    public void startTracking() {
        prepareHelper();
        restartCounter();
        trackingHelper.startTracking();
        Movit.getApp().setTracking(true);
    }

    public void stopTracking() {
        trackingHelper.stopTracking();
        counter.stop();
        Movit.getApp().setTracking(false);
    }

    private void restartCounter() {
        counter.stop();
        counter.reset();
        counter.start();
    }

    private void init() {
        movement = Movit.getApp().getInitialLocation();
        if (movement != null) {
            currentActivity = movement.getActivityName();
        }
        counter = new CountUpTimer();
        counter.setListener(getTickListener());
        prepareHelper();
    }

    private TimerTickListener getTickListener() {
        return new TimerTickListener() {
            @Override
            public void onTick(long elapsedTime) {
                if (trackingHelper.hasTimeElapsed(elapsedTime)
                        && !trackingHelper.isCurrentActivityLogged()) {
                    trackingHelper.logCurrentActivity(currentActivity);
                }
                timeElapsed = elapsedTime;
                broadcastLocation(movement);
            }
        };
    }

    private void prepareHelper() {
        trackingHelper = new TrackingHelper(Movit.getApp());
        trackingHelper.setMovement(movement);
        trackingHelper.setCurrentActivityLogged(true);
        trackingHelper.setLocationCallback(getLocationCallback());
        trackingHelper.setActivityCallback(getActivityCallback());
        trackingHelper.setTimeBeforeLogging(PreferenceHelper.getTimeBeforeLogging());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(Constants.COMMAND.getValue());
        if (command != null) {
            executeCommand(command);
        }
    }

    private void executeCommand(String command) {
        switch (command) {
            case "START":
                startTracking();
                break;
            case "STOP":
                stopTracking();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopTracking();
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationDetected(Movement mv) {
                movement = mv;
                trackingHelper.setMovement(mv);
                broadcastLocation(mv);
            }
        };
    }

    private void broadcastLocation(Movement movement) {
        if (movement != null) {
            Intent intent = new Intent(Constants.LOCATION.getValue());
            intent = Utility.putMovementInIntent(movement, intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private IncomingStringCallback getActivityCallback() {
        return new IncomingStringCallback() {
            @Override
            public void onStringArrive(String activityName) {
                if (trackingHelper.hasActivityChanged(activityName)) {
                    restartCounter();
                    currentActivity = activityName;
                    trackingHelper.setCurrentActivity(currentActivity);
                }
                broadcastActivityStatement();
            }
        };
    }

    private void broadcastActivityStatement() {
        Intent broadcastIntent = new Intent(Constants.STATEMENT.getValue());
        broadcastIntent.putExtra(
                Constants.STATEMENT.getValue(),
                trackingHelper.getActivityStatement(currentActivity));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
