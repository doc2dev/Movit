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

    private boolean isActive;

    private String currentActivity = "Unknown";

    private long timeBeforeLogging;

    public TrackingHelper(Context context) {
        this.context = context;
        initializeVariables();
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public void setCurrentActivity(String activity) {
        currentActivity = activity;
    }

    public void setTimeBeforeLogging(long timeBeforeLogging) {
        this.timeBeforeLogging = timeBeforeLogging;
    }

    public void setActivityCallback(IncomingStringCallback activityCallback) {
        recognitionHelper.setActivityCallback(activityCallback);
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        locationHelper.setLocationCallback(locationCallback);
    }

    private void initializeVariables() {
        locationHelper = new LocationHelper(context);
        recognitionHelper = new RecognitionHelper(context);
        currentActivity = "Unknown";
    }

    public void logCurrentActivity(String activity, long elapsedTime) {
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

    public void startTracking() {
        locationHelper.connect();
        recognitionHelper.connect();
        isActive = true;
    }

    public void stopTracking() {
        locationHelper.disconnect();
        recognitionHelper.disconnect();
        isActive = false;
    }

    public boolean hasTimeElapsed(long timeElapsed) {
        return timeElapsed >= timeBeforeLogging;
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
