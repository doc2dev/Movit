package com.andela.movit.listeners;


import com.google.android.gms.location.DetectedActivity;

public interface ActivityCallback {
    void onActivityDetected(String activityName);
}
