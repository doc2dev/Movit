package com.andela.movit.async;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.andela.movit.config.Constants;
import com.andela.movit.listeners.IncomingStringCallback;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.PreferenceHelper;
import com.andela.movit.utilities.TrackingHelper;
import com.andela.movit.utilities.Utility;

public class TrackingService extends Service {

    private Movement movement;

    private String currentActivity = "Unknown";

    private TrackingHelper trackingHelper;

    private CountUpTimer counter;

    private TrackingBinder trackingBinder;

    private long timeElapsed;

    public void startTracking() {
        prepareHelper();
        restartCounter();
        trackingHelper.startTracking();
    }

    public void stopTracking() {
        trackingHelper.stopTracking();
        counter.stop();
    }

    private void restartCounter() {
        counter.stop();
        counter.start();
    }

    private void init(Intent intent) {
        movement = Utility.getMovementFromBundle(intent.getExtras());
        currentActivity = movement.getActivityName();
        counter = new CountUpTimer();
        counter.setListener(getTickListener());
        trackingBinder = new TrackingBinder();
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
            }
        };
    }

    private void prepareHelper() {
        trackingHelper = new TrackingHelper(this);
        trackingHelper.setMovement(movement);
        trackingHelper.setCurrentActivityLogged(true);
        trackingHelper.setLocationCallback(getLocationCallback());
        trackingHelper.setActivityCallback(getActivityCallback());
        trackingHelper.setTimeBeforeLogging(PreferenceHelper.getTimeBeforeLogging());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return trackingBinder;
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
        Intent intent  = new Intent(Constants.LOCATION.getValue());
        intent = Utility.putMovementInIntent(movement, intent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private IncomingStringCallback getActivityCallback() {
        return new IncomingStringCallback() {
            @Override
            public void onStringArrive(String activityName) {
                if (trackingHelper.hasActivityChanged(activityName)) {
                    restartCounter();
                    currentActivity = activityName;
                    trackingHelper.setCurrentActivity(currentActivity);
                    broadcastActivityStatement();
                }
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

    public boolean isTracking() {
        return trackingHelper.isActive();
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public class TrackingBinder extends Binder {
        public TrackingService getService() {
            return TrackingService.this;
        }
    }
}
