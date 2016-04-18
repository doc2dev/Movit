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

import com.andela.movit.R;

import com.andela.movit.activities.TrackerActivity;
import com.andela.movit.listeners.MovementCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.MovementHelper;
import com.andela.movit.utilities.Utility;


public class SplashFragment extends Fragment {

    private TextView appName;

    private View view;

    private Activity activity;

    private MovementHelper movementHelper;

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
        movementHelper = MovementHelper.getMovementHelper();
        movementHelper.setContext(activity);
        movementHelper.setMovementCallback(getMovementCallback());
    }

    @Override
    public void onStart() {
        super.onStart();
        movementHelper.apiInit();
        movementHelper.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        movementHelper.disconnect();
    }

    private MovementCallback getMovementCallback() {
        return new MovementCallback() {
            @Override
            public void onMovementDetected(Movement movement) {
                movementHelper.setIdle(true);
                doTransitionToTracker(movement);
            }

            @Override
            public void onError(String message) {
                Utility.makeToast(activity, message);
            }
        };
    }

    private void doTransitionToTracker(Movement movement) {
        Utility.launchActivityWithMovement(activity, TrackerActivity.class, movement);
        movementHelper.disconnect();
        activity.finish();
    }
}