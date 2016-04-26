package com.andela.movit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.movit.Movit;
import com.andela.movit.R;
import com.andela.movit.activities.SplashActivity;
import com.andela.movit.background.TrackingService;
import com.andela.movit.listeners.IncomingStringCallback;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.receivers.LocationBroadcastReceiver;
import com.andela.movit.receivers.StringBroadcastReceiver;
import com.andela.movit.utilities.Utility;

import pl.droidsonroids.gif.GifImageView;

import static com.andela.movit.config.Constants.*;

public class TrackerFragment extends Fragment {

    private View rootView;

    private Activity context;

    private TextView locationName;

    private TextView locationCoords;

    private ImageView lockStatic;

    private GifImageView lockAnim;

    private View notifyContainer;

    private TextView activityNameView;

    private Button trackButton;

    private String currentActivity = "Unknown";

    private Chronometer counter;

    private Movement movement;

    private StringBroadcastReceiver statementReceiver;

    private LocationBroadcastReceiver locationReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.rootView = view;
        context = getActivity();
        start();
    }

    private void start() {
        Movit app = Movit.getApp();
        if (app.isAppLaunched()) {
            initializeComponents();
            movement = app.getInitialLocation();
            displayLocation(movement);
            app.setIdle(true);
        } else {
            launchSplash();
        }
    }

    private void launchSplash() {
        Utility.launchActivity(context, SplashActivity.class);
        context.finish();
    }

    private void initializeComponents() {
        initializeViews();
        setTrackClickListener();
    }

    private void initializeViews() {
        locationName = (TextView) rootView.findViewById(R.id.location_name);
        locationCoords = (TextView) rootView.findViewById(R.id.location_coords);
        activityNameView = (TextView) rootView.findViewById(R.id.activity_name);
        trackButton = (Button) rootView.findViewById(R.id.trackButton);
        notifyContainer = rootView.findViewById(R.id.notify_container);
        lockStatic = (ImageView) rootView.findViewById(R.id.lock_static);
        lockAnim = (GifImageView) rootView.findViewById(R.id.lock_gif);
        counter = (Chronometer) rootView.findViewById(R.id.counter);
    }

    private void setTrackClickListener() {
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTracking();
            }
        });
    }

    private void toggleTracking() {
        if (Movit.getApp().isTracking()) {
            stopTracking();
        } else {
            startTracking();
        }
    }

    private void stopTracking() {
        sendCommandToService("STOP");
        toggleViews(false);
        counter.stop();
    }

    private void sendCommandToService(String command) {
        Intent intent  = new Intent(context, TrackingService.class);
        intent.putExtra(COMMAND.getValue(), command);
        context.startService(intent);
    }

    private void startTracking() {
        sendCommandToService("START");
        toggleViews(true);
        restartCounter();
    }

    private void restartCounter() {
        counter.setBase(SystemClock.elapsedRealtime());
        counter.start();
    }

    private void toggleViews(boolean isTracking) {
        if (isTracking) {
            notifyContainer.setVisibility(View.VISIBLE);
            trackButton.setText(R.string.label_stop_tracking);
            toggleGiphy(true);
        } else {
            notifyContainer.setVisibility(View.INVISIBLE);
            trackButton.setText(R.string.label_start_tracking);
            toggleGiphy(false);
        }
    }

    private void registerStatementReceiver() {
        statementReceiver = Utility.registerStringReceiver(context, STATEMENT.getValue());
        statementReceiver.setIncomingStringCallback(statementCallback);
    }

    private void registerLocationReceiver() {
        locationReceiver = new LocationBroadcastReceiver(locationCallback);
        IntentFilter filter = new IntentFilter(LOCATION.getValue());
        LocalBroadcastManager.getInstance(context).registerReceiver(locationReceiver, filter);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationDetected(Movement mv) {
            movement = mv;
            displayLocation(mv);
        }
    };

    private IncomingStringCallback statementCallback = new IncomingStringCallback() {
        @Override
        public void onStringArrive(String activityStatement) {
            displayActivity(activityStatement);
            Movit.getApp().setIdle(true);
        }
    };

    private void toggleGiphy(boolean isTracking) {
        if (isTracking) {
            lockStatic.setVisibility(View.GONE);
            lockAnim.setVisibility(View.VISIBLE);
        } else {
            lockStatic.setVisibility(View.VISIBLE);
            lockAnim.setVisibility(View.GONE);
        }
    }

    private void displayLocation(Movement mv) {
        locationName.setText(mv.getPlaceName());
        locationCoords.setText(Utility.getCoordsString(mv));
    }

    private void displayActivity(String activity) {
        if (!currentActivity.equals(activity)) {
            activityNameView.setText(activity);
            currentActivity = activity;
            restartCounter();
        }
    }

    @Override
    public void onPause() {
        Utility.unregisterReceiver(context, locationReceiver);
        Utility.unregisterReceiver(context, statementReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        toggleViews(Movit.getApp().isTracking());
        registerLocationReceiver();
        registerStatementReceiver();
        super.onResume();
    }
}
