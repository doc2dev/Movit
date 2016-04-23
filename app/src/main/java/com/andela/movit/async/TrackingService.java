package com.andela.movit.async;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.andela.movit.Movit;
import com.andela.movit.R;
import com.andela.movit.activities.TrackerActivity;
import com.andela.movit.listeners.ActivityCallback;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.PreferenceHelper;
import com.andela.movit.utilities.TrackingHelper;
import com.andela.movit.utilities.Utility;

public class TrackingService extends Service {

    private Movement movement;

    private String currentActivity;

    private TrackingHelper trackingHelper;

    private  CountUpTimer countUpTimer;

    private long baseTime;

    public TrackingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init(intent);
        startTracking();
        Movit.getApp().setTrackingServiceRunning(true);
        notifyActivity();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTracking() {
        countUpTimer.setBaseTime(movement.getTimeStamp());
        countUpTimer.start();
        trackingHelper.startTracking();
    }

    private void init(Intent intent) {
        movement = Utility.getMovementFromBundle(intent.getExtras());
        currentActivity = movement.getActivityName();
        trackingHelper = new TrackingHelper(this);
        trackingHelper.setMovement(movement);
        trackingHelper.setLocationCallback(getLocationCallback());
        trackingHelper.setActivityCallback(getActivityCallback());
        trackingHelper.setTimeBeforeLogging(PreferenceHelper.getTimeBeforeLogging());
        countUpTimer = new CountUpTimer();
        countUpTimer.setListener(getTickListener());
    }

    private TimerTickListener getTickListener() {
        return new TimerTickListener() {
            @Override
            public void onTick(long elapsedTime) {
                if (trackingHelper.hasTimeElapsed(elapsedTime)
                        && !trackingHelper.isCurrentActivityLogged()) {
                    trackingHelper.logCurrentActivity(currentActivity);
                }
                baseTime = elapsedTime;
                storeCurrentMovement();
            }
        };
    }

    private void storeCurrentMovement() {
        movement.setActivityName(currentActivity);
        movement.setTimeStamp(baseTime);
        Movit.getApp().setMovement(movement);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopTracking();
        Movit.getApp().setTrackingServiceRunning(false);
    }

    private void stopTracking() {
        trackingHelper.stopTracking();
        countUpTimer.stop();
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationDetected(Movement mv) {
                movement = mv;
                trackingHelper.setMovement(mv);
            }
        };
    }

    private ActivityCallback getActivityCallback() {
        return new ActivityCallback() {
            @Override
            public void onActivityDetected(String activityName) {
                if (trackingHelper.hasActivityChanged(activityName)) {
                    restartCounter();
                    currentActivity = activityName;
                    trackingHelper.setCurrentActivity(currentActivity);
                    notifyActivity();
                }
            }
        };
    }

    private void notifyActivity() {
        Notification notification = getNotification();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private Notification getNotification() {
        return new Notification.Builder(this)
                .setContentTitle("Tracking in Progress")
                .setContentInfo(getStatement(currentActivity))
                .setContentIntent(getIntent())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();
    }

    private PendingIntent getIntent() {
        Intent intent = new Intent(this, TrackerActivity.class);
        return PendingIntent.getActivity(this, 1000, intent, 0);
    }

    private String getStatement(String currentActivity) {
        return "You are currently " + currentActivity + ".";
    }

    private void restartCounter() {
        countUpTimer.stop();
        countUpTimer.setBaseTime(SystemClock.elapsedRealtime());
        countUpTimer.start();
    }
}
