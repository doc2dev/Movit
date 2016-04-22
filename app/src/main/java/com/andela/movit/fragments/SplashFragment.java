package com.andela.movit.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.movit.Movit;
import com.andela.movit.R;

import com.andela.movit.activities.TrackerActivity;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.location.LocationHelper;
import com.andela.movit.utilities.Utility;


public class SplashFragment extends Fragment {

    private TextView appName;

    private View view;

    private Activity activity;

    private LocationHelper locationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        activity = getActivity();
        initializeComponents();
    }

    private void initializeComponents() {
        appName = (TextView) view.findViewById(R.id.app_name);
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/custom.ttf");
        appName.setTypeface(typeface);
        locationHelper = new LocationHelper(activity);
        locationHelper.setLocationCallback(getMovementCallback());
    }

    @Override
    public void onStart() {
        super.onStart();
        locationHelper.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationHelper.disconnect();
    }

    private LocationCallback getMovementCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationDetected(Movement movement) {
                Movit.getApp().setIdle(true);
                doTransitionToTracker(movement);
            }
        };
    }

    private void doTransitionToTracker(Movement movement) {
        Utility.launchActivityWithMovement(activity, TrackerActivity.class, movement);
        locationHelper.disconnect();
        activity.finish();
    }
}