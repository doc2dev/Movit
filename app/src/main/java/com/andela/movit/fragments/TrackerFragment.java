package com.andela.movit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.andela.movit.async.TrackingService;
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

    private Bundle bundle;

    private String currentActivity = "Unknown";

    private Chronometer counter;

    private Movement movement;

    private TrackingService trackingService;

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
        Bundle bundle = context.getIntent().getExtras();
        start(bundle);
    }

    private void start(Bundle bundle) {
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

    private void init() {
        initializeViews();
        displayLocation(movement);
        setTrackClickListener();
        bindTrackingService();
    }

    private void bindTrackingService() {
        trackingService = Movit.getApp().getTrackingService();
        if (trackingService == null) {
            bindNewInstance();
        } else {
            continueTracking();
        }
    }

    private void bindNewInstance() {
        Intent intent = new Intent(context, TrackingService.class);
        intent = Utility.putMovementInIntent(movement, intent);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void continueTracking() {
        long elapsedTime = trackingService.getTimeElapsed();
        if (trackingService.isTracking() && elapsedTime > 0) {
            registerReceivers();
            toggleViews(true);
            startCounterFrom(elapsedTime);
            displayActivity(trackingService.getCurrentActivity());
        }
    }

    private void startCounterFrom(long elapsedTime) {
        counter.setBase(SystemClock.elapsedRealtime() - elapsedTime);
        counter.start();
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
        if (trackingService.isTracking()) {
            stopTracking();
        } else {
            startTracking();
        }
    }

    private void stopTracking() {
        trackingService.stopTracking();
        unregisterReceivers();
        toggleViews(false);
        counter.stop();
    }

    private void startTracking() {
        trackingService.startTracking();
        registerReceivers();
        toggleViews(true);
        restartCounter();
    }

    private void unregisterReceivers() {
        Utility.unregisterReceiver(context, locationReceiver);
        Utility.unregisterReceiver(context, statementReceiver);
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

    private void registerReceivers() {
        statementReceiver = Utility.registerStringReceiver(context, STATEMENT.getValue());
        statementReceiver.setIncomingStringCallback(statementCallback);
        registerLocationReceiver();
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

    private void displayActivity(String activity) {
        if (!currentActivity.equals(activity)) {
            activityNameView.setText(activity);
            restartCounter();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            trackingService = ((TrackingService.TrackingBinder)service).getService();
            Movit.getApp().setTrackingService(trackingService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
