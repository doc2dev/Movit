/**
 * This class wraps the respective location and recognition helpers to co-ordinate tracking
 * activities. It provides start and stop methods for tracking, and setters for the current
 * movement object (which stores the current location) and the current activity. It also provides
 * a method for logging the current activity.
 * */

package com.andela.movit.utilities;

import android.content.Context;

import com.andela.movit.R;
import com.andela.movit.activityrecognition.RecognitionHelper;
import com.andela.movit.data.DbAsync;
import com.andela.movit.data.DbCallback;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;
import com.andela.movit.data.DbRepo;
import com.andela.movit.location.IncomingStringCallback;
import com.andela.movit.location.LocationCallback;
import com.andela.movit.location.LocationHelper;
import com.andela.movit.models.Movement;

public class TrackingHelper {

    private Context context;

    private LocationHelper locationHelper;

    private RecognitionHelper recognitionHelper;

    private Movement movement;

    private String currentActivity = "Unknown";

    private long durationBeforeLogging;

    public TrackingHelper(Context context) {
        this.context = context;
        initializeVariables();
    }

    private void initializeVariables() {
        locationHelper = new LocationHelper(context);
        recognitionHelper = new RecognitionHelper(context);
        currentActivity = "Unknown";
    }

    /**
     * Stores a {@code Movement} object that holds the current location.
     * @param movement the movement object containing details of the current location.
     * */

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    /**
     * Stores the name of the current activity.
     * @param activity the current activity.
     * */

    public void setCurrentActivity(String activity) {
        currentActivity = activity;
    }

    /**
     * Sets the minimum duration for which an activity has to be performed before it can be logged.
     * @param durationBeforeLogging the duration before logging (in milliseconds);
     * */

    public void setDurationBeforeLogging(long durationBeforeLogging) {
        this.durationBeforeLogging = durationBeforeLogging;
    }

    /**
     * Sets the callback to be invoked whenever an activity is detected.
     * @param activityCallback the callback object.
     * */

    public void setActivityCallback(IncomingStringCallback activityCallback) {
        recognitionHelper.setActivityCallback(activityCallback);
    }

    /**
     * Sets the callback to be invoked whenever a new location is detected.
     * @param locationCallback the callback object.
     * */

    public void setLocationCallback(LocationCallback locationCallback) {
        locationHelper.setLocationCallback(locationCallback);
    }

    /**
     * Appends the an activity name to the current stored {@code Movement} object, then writes
     * it to the database.
     * */

    public void logActivity(String activity, long elapsedTime) {
        if (!isActivityUnknown(activity)) {
            movement.setActivityName(activity);
            movement.setDuration(elapsedTime);
            writeMovementToDatabase(movement);
        }
    }

    private boolean isActivityUnknown(String activ) {
        return activ.equals("Unknown");
    }

    private void writeMovementToDatabase(Movement movement) {
        movement.setTimeStamp(System.currentTimeMillis());
        DbAsync dbAsync = new DbAsync(getDbCallback());
        dbAsync.execute(getInsertOperation(movement));
    }

    /**
     * Starts the tracking process.
     * */

    public void startTracking() {
        locationHelper.connect();
        recognitionHelper.connect();
    }

    /**
     * Stops the tracking process.
     * */

    public void stopTracking() {
        locationHelper.disconnect();
        recognitionHelper.disconnect();
    }

    public boolean hasTimeElapsed(long timeElapsed) {
        return timeElapsed >= durationBeforeLogging;
    }

    public boolean hasActivityChanged(String activityName) {
        return !currentActivity.equals(activityName);
    }

    private DbCallback getDbCallback() {
        return new DbCallback() {
            @Override
            public void onOperationSuccess(Object result) {
                FrameworkUtils.makeToast(context, "Activity logged successfully");
            }

            @Override
            public void onOperationFail(String errorMessage) {
            }
        };
    }

    /**
     * Returns a string representing an activity.
     * */

    public String getActivityStatement(String activityName) {
        if (isActivityUnknown(activityName)) {
            return context.getString(R.string.label_activity_name);
        }
        return "Current status: " + activityName;
    }

    private DbOperation getInsertOperation(final Movement movement) {
        return new DbOperation() {
            @Override
            public DbResult execute() {
                DbRepo repo = new DbRepo(context);
                return new DbResult(repo.addMovement(movement), null);
            }
        };
    }
}
