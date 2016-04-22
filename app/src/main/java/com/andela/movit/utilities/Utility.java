package com.andela.movit.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    public static Movement getMovementFromBundle(Bundle bundle) {
        Movement mv = new Movement();
        mv.setActivityName(getStringFromBundle(bundle, Constants.ACTIVITY_NAME.getValue()));
        mv.setPlaceName(getStringFromBundle(bundle, Constants.PLACE_NAME.getValue()));
        mv.setLatitude(
                Double.parseDouble(getStringFromBundle(bundle, Constants.LATITUDE.getValue())));
        mv.setLongitude(
                Double.parseDouble(getStringFromBundle(bundle, Constants.LONGITUDE.getValue())));
        return mv;
    }

    private static String getStringFromBundle(Bundle bundle, String name) {
        return bundle.getString(name);
    }

    public static String getCoordsString(Movement movement) {
        return Double.toString(movement.getLatitude())
                + ", "
                + Double.toString(movement.getLongitude());
    }
}
