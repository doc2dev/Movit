package com.andela.movit.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.andela.movit.Movit;
import com.andela.movit.models.Movement;
import com.andela.movit.receivers.StringBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.andela.movit.config.Constants.*;

public class Utility {

    public static String getCoordsString(Movement movement) {
        return Double.toString(movement.getLatitude())
                + ", "
                + Double.toString(movement.getLongitude());
    }

    public static int[] getDateValues(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[] {year, month, day};
    }

    public static Date generateDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public static String getDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
        return format.format(date);
    }

    public static String getDurationString(long duration) {
        long longVal = duration / 1000;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return formatNumber(hours) + ":" + formatNumber(mins) + ":" + formatNumber(secs);
    }

    private static String formatNumber(int number) {
        String numString = Integer.toString(number);
        return number < 9 ? "0" + numString : numString;
    }
}
