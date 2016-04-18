package com.andela.movit.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import com.andela.movit.listeners.MovementCallback;
import com.andela.movit.listeners.PlaceNameCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.test_support.TestableAsync;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MovementHelper extends TestableAsync implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;

    private GoogleApiClient apiClient;

    private LocationRequest locationRequest;

    private MovementCallback movementCallback;

    private static MovementHelper movementHelper;

    public void setContext(Context context) {
        this.context = context;
    }

    public static MovementHelper getMovementHelper() {
        if (movementHelper == null) {
            movementHelper = new MovementHelper();
        }
        return movementHelper;
    }

    public void setMovementCallback(MovementCallback movementCallback) {
        this.movementCallback = movementCallback;
    }

    public void apiInit() {
        initializeApiClient();
    }

    public void connect() {
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
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void initializeApiClient() {
        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
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
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        try {
            addresses
                    = geocoder.getFromLocation(movement.getLatitude(), movement.getLongitude(), 1);
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
                movement.setPlaceName(address.getFeatureName());
                movementCallback.onMovementDetected(movement);
            }
        }
        catch (Exception exception) {
            useGeoHelper(movement);
        }
    }

    private void useGeoHelper(final Movement movement) {
        GeoHelper helper = new GeoHelper(context);
        helper.setCallback(getPlaceNameCallback(movement));
        helper.getPlaceName(movement.getLatitude(), movement.getLongitude());
    }

    private PlaceNameCallback getPlaceNameCallback(final Movement movement) {
        return new PlaceNameCallback() {
            @Override
            public void onPlaceNameFound(String placeName) {
                movement.setPlaceName(placeName);
                movementCallback.onMovementDetected(movement);
            }

            @Override
            public void onError(String message) {
                movementCallback.onError(message);
            }
        };
    }
}
