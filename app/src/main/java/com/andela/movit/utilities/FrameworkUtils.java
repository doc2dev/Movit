package com.andela.movit.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.andela.movit.models.Movement;
import com.andela.movit.receivers.StringBroadcastReceiver;

import static com.andela.movit.config.Constants.ACTIVITY_NAME;
import static com.andela.movit.config.Constants.LATITUDE;
import static com.andela.movit.config.Constants.LONGITUDE;
import static com.andela.movit.config.Constants.PLACE_NAME;
import static com.andela.movit.config.Constants.TIMESTAMP;

public class FrameworkUtils {

    public static void startActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void stopService(Context context, Class serviceClass) {
        context.stopService(new Intent(context, serviceClass));
    }

    public static Intent putMovementInIntent(Movement movement, Intent intent) {
        intent.putExtra(PLACE_NAME.getValue(), movement.getPlaceName());
        intent.putExtra(ACTIVITY_NAME.getValue(), movement.getActivityName());
        intent.putExtra(LATITUDE.getValue(), Double.toString(movement.getLatitude()));
        intent.putExtra(LONGITUDE.getValue(), Double.toString(movement.getLongitude()));
        intent.putExtra(TIMESTAMP.getValue(), Long.toString(movement.getTimeStamp()));
        return intent;
    }

    public static Movement getMovementFromBundle(Bundle bundle) {
        Movement mv = new Movement();
        mv.setActivityName(getStringFromBundle(bundle, ACTIVITY_NAME.getValue()));
        mv.setPlaceName(getStringFromBundle(bundle, PLACE_NAME.getValue()));
        mv.setLatitude(Double.parseDouble(getStringFromBundle(bundle, LATITUDE.getValue())));
        mv.setLongitude(Double.parseDouble(getStringFromBundle(bundle, LONGITUDE.getValue())));
        mv.setTimeStamp(Long.parseLong(getStringFromBundle(bundle, TIMESTAMP.getValue())));
        return mv;
    }

    private static String getStringFromBundle(Bundle bundle, String name) {
        return bundle.getString(name);
    }

    public static StringBroadcastReceiver registerStringReceiver(Context context, String actionName) {
        StringBroadcastReceiver receiver = new StringBroadcastReceiver(actionName);
        LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(receiver, getFilter(actionName));
        return receiver;
    }

    private static IntentFilter getFilter(String actionName) {
        return new IntentFilter(actionName);
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static LayoutInflater getInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static void broadcastIntent(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
