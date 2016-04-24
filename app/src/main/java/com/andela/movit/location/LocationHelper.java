package com.andela.movit.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.listeners.IncomingStringCallback;
import com.andela.movit.models.Movement;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;

    private GoogleApiClient apiClient;

    private LocationRequest locationRequest;

    private LocationCallback locationCallback;

    public LocationHelper(Context context) {
        this.context = context;
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public void connect() {
        if (apiClient == null) {
            initializeApiClient();
        }
        apiClient.connect();
    }

    private void initializeLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setSmallestDisplacement(5f);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (apiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private synchronized void initializeApiClient() {
        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initializeLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Movement movement = new Movement();
        movement.setLatitude(location.getLatitude());
        movement.setLongitude(location.getLongitude());
        getPlaceName(movement);
    }

    private void getPlaceName(Movement movement) {
        GeoHelper helper = new GeoHelper(context);
        helper.setCallback(getPlaceNameCallback(movement));
        helper.getPlaceName(movement.getLatitude(), movement.getLongitude());
    }

    private IncomingStringCallback getPlaceNameCallback(final Movement movement) {
        return new IncomingStringCallback() {
            @Override
            public void onStringArrive(String placeName) {
                movement.setPlaceName(placeName);
                locationCallback.onLocationDetected(movement);
            }
        };
    }
}
