package com.andela.movit.activityrecognition;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.andela.movit.config.Constants;
import com.andela.movit.listeners.IncomingStringCallback;
import com.andela.movit.receivers.StringBroadcastReceiver;
import com.andela.movit.utilities.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;

public class RecognitionHelper implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    private IncomingStringCallback activityCallback;

    private StringBroadcastReceiver activityBroadcastReceiver;

    private Context context;

    private GoogleApiClient apiClient;

    private PendingIntent pendingIntent;

    public RecognitionHelper(Context context) {
        this.context = context;
        buildApiClient();
    }

    public void setActivityCallback(IncomingStringCallback activityCallback) {
        this.activityCallback = activityCallback;
    }

    public void connect() {
        registerReceiver();
        apiClient.connect();
    }

    public void disconnect() {
        if (apiClient.isConnected()) {
            unregisterReceiver();
            stopActivityRecognition();
            apiClient.disconnect();
        }
    }

    private void unregisterReceiver() {
        Utility.unregisterReceiver(context, activityBroadcastReceiver);
    }

    private void buildApiClient() {
        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build();
    }

    private void registerReceiver() {
        activityBroadcastReceiver = Utility.registerStringReceiver(
                context, Constants.ACTIVITY_NAME.getValue());
        activityBroadcastReceiver.setIncomingStringCallback(activityCallback);
        LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(activityBroadcastReceiver, getFilter());
    }

    private IntentFilter getFilter() {
        return new IntentFilter(Constants.ACTIVITY_NAME.getValue());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startActivityRecognition();
    }

    private void startActivityRecognition() {
        pendingIntent = getPendingIntent();
        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(apiClient, 0, pendingIntent);
    }

    private void stopActivityRecognition() {
        ActivityRecognition.ActivityRecognitionApi
                .removeActivityUpdates(apiClient, pendingIntent);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, ActivityRecognitionService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(@NonNull Status status) {
    }
}
