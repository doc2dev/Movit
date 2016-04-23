package com.andela.movit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.andela.movit.async.TrackingService;
import com.andela.movit.listeners.ActivityCallback;
import com.andela.movit.listeners.LocationCallback;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.PreferenceHelper;
import com.andela.movit.utilities.TrackingHelper;
import com.andela.movit.utilities.Utility;

import pl.droidsonroids.gif.GifImageView;

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

    private Bundle bundle;

    private Chronometer counter;

    private String currentActivity = "Unknown";

    private Movement movement;

    private TrackingHelper trackingHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.rootView = view;
        context = getActivity();
        Bundle bundle = context.getIntent().getExtras();
        start(bundle);
    }

    private void resume() {
        Utility.stopService(context, TrackingService.class);
        Movit.getApp().setTrackingServiceRunning(false);
        movement = Movit.getApp().getMovement();
        resumeTracking();
    }

    private void start(Bundle bundle) {

        if (Movit.getApp().isTrackingServiceRunning()) {
            resume();
        } else {
            if (bundle == null) {
                Utility.launchActivity(context, SplashActivity.class);
                context.finish();
            } else  {
                Movit.getApp().setIdle(true);
                this.bundle = bundle;
                movement = Utility.getMovementFromBundle(bundle);
                init();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Movit.getApp().isTrackingServiceRunning()) {
            resume();
        }
    }

    private void init() {
        initializeComponents();
        initializeVariables();
        displayLocation(movement);
        setTrackClickListener();
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
        if (trackingHelper.isActive()) {
            stopTracking();
        } else {
            startTracking();
        }
    }

    private void stopTracking() {
        stopCounter();
        trackingHelper.stopTracking();
        trackButton.setText(R.string.label_start_tracking);
        notifyContainer.setVisibility(View.INVISIBLE);
        toggleGiphy(false);
    }

    private void startTracking() {
        restartCounter(SystemClock.elapsedRealtime());
        trackingHelper.startTracking();
        trackButton.setText(R.string.label_stop_tracking);
        notifyContainer.setVisibility(View.VISIBLE);
        toggleGiphy(true);
    }

    private void resumeTracking() {
        init();
        restartCounter(Utility.calculateResumeTime(movement.getTimeStamp()));
        trackingHelper.setMovement(movement);
        currentActivity = movement.getActivityName();
        trackingHelper.setCurrentActivity(currentActivity);
        displayActivity(trackingHelper.getActivityStatement(currentActivity));
        trackingHelper.startTracking();
        trackButton.setText(R.string.label_stop_tracking);
        notifyContainer.setVisibility(View.VISIBLE);
        toggleGiphy(true);
    }


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
        trackingHelper.setMovement(mv);
    }

    private void initializeComponents() {
        initializeVariables();
        initializeViews();
    }

    @Override
    public void onPause() {
        if (trackingHelper.isActive()) {
            moveTrackingToService();
        }
        super.onPause();
    }

    private void moveTrackingToService() {
        movement.setActivityName(currentActivity);
        movement.setTimeStamp(counter.getBase());
        Utility.launchService(context, TrackingService.class, movement);
        Movit.getApp().setTrackerActive(true);
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
        counter.setOnChronometerTickListener(getTickListener());
    }

    private void initializeVariables() {
        trackingHelper = new TrackingHelper(context);
        trackingHelper.setLocationCallback(getLocationCallback());
        trackingHelper.setActivityCallback(getActivityCallback());
        trackingHelper.setTimeBeforeLogging(PreferenceHelper.getTimeBeforeLogging());
    }

    private void restartCounter(long baseTime) {
        counter.setBase(baseTime);
        counter.start();
    }

    private void stopCounter() {
        counter.stop();
    }

    private void displayActivity(String activity) {
        activityNameView.setText(activity);
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationDetected(Movement mv) {
                displayLocation(mv);
            }
        };
    }

    private ActivityCallback getActivityCallback() {
        return new ActivityCallback() {
            @Override
            public void onActivityDetected(String activityName) {
                if (trackingHelper.hasActivityChanged(activityName)) {
                    restartCounter(SystemClock.elapsedRealtime());
                    currentActivity = activityName;
                    trackingHelper.setCurrentActivity(currentActivity);
                    displayActivity(trackingHelper.getActivityStatement(activityName));
                    Movit.getApp().setIdle(true);
                }
            }
        };
    }

    public Chronometer.OnChronometerTickListener getTickListener() {
        return new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (trackingHelper.hasTimeElapsed(chronometer.getBase())
                        && !trackingHelper.isCurrentActivityLogged()) {
                    trackingHelper.logCurrentActivity(currentActivity);
                }
            }
        };
    }
}
