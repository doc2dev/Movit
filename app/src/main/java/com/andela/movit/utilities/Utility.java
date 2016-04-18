package com.andela.movit.utilities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.andela.movit.Movit;
import com.andela.movit.activities.TrackerActivity;
import com.andela.movit.config.Constants;
import com.andela.movit.models.Movement;

public class Utility {

    public static Movit getApp() {
        return Movit.getApp();
    }

    public static void launchActivity(Activity activity, Class activityClass) {
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
    }

    public static void makeToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void launchActivityWithMovement(Activity activity, Class activityClass,
                                                  Movement movement) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(Constants.PLACE_NAME.getValue(), movement.getPlaceName());
        intent.putExtra(Constants.ACTIVITY_NAME.getValue(), movement.getActivityName());
        intent.putExtra(Constants.LATITUDE.getValue(), Double.toString(movement.getLatitude()));
        intent.putExtra(Constants.LONGITUDE.getValue(), Double.toString(movement.getLongitude()));
        activity.startActivity(intent);
    }
}
