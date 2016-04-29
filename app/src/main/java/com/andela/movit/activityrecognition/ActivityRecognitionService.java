/**
 * This class handles the activity updates coming in from the Google PLay Activity Recognition API.
 * Every time an activity update comes in, it broadcasts it.
 * */

package com.andela.movit.activityrecognition;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.andela.movit.config.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import static com.google.android.gms.location.DetectedActivity.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityRecognitionService extends IntentService {

    public ActivityRecognitionService() {
        super(Constants.SERVICE_NAME.getValue());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        probableActivities = sortActivities(probableActivities);
        String activityName = getActivityName(probableActivities.get(0));
        broadcastActivityName(activityName);
    }

    private List<DetectedActivity> sortActivities(List<DetectedActivity> probableActivities) {
        Collections.sort(probableActivities, getComparator());
        return probableActivities;
    }

    private Comparator<? super DetectedActivity> getComparator() {
        return new Comparator<DetectedActivity>() {
            @Override
            public int compare(DetectedActivity lhs, DetectedActivity rhs) {
                return rhs.getConfidence() - lhs.getConfidence();
            }
        };
    }

    private void broadcastActivityName(String activityName) {
        Intent broadcastIntent = new Intent(Constants.ACTIVITY_NAME.getValue());
        broadcastIntent.putExtra(Constants.ACTIVITY_NAME.getValue(), activityName);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private boolean hasResult(Intent intent) {
        return ActivityRecognitionResult.hasResult(intent);
    }

    public String getActivityName(DetectedActivity activity) {
        switch (activity.getType()) {
            case IN_VEHICLE:
                return "Travelling";
            case ON_BICYCLE:
                return "Cycling";
            case ON_FOOT:
                return "Walking";
            case RUNNING:
                return "Running";
            case STILL:
                return "Standing Still";
            case TILTING:
                return "Standing Still";
            case WALKING:
                return "Walking";
            default:
                return "Unknown";
        }
    }
}
