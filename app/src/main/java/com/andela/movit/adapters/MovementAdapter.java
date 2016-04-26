package com.andela.movit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.models.Movement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.SimpleFormatter;

public class MovementAdapter extends ArrayAdapter<Movement> {

    private Context context;

    private List<Movement> movements;

    private int viewId;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public MovementAdapter(Context context, int resource, List<Movement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.movements = objects;
        this.viewId = resource;
    }

    public void setMovements(List<Movement> movts) {
        movements.clear();
        for (Movement movement : movts) {
            movements.add(movement);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getInflater().inflate(viewId, null);
        Movement movement = movements.get(position);
        TextView actView = (TextView)view.findViewById(R.id.label_activity);
        String activity = movement.getActivityName();
        actView.setText(activity);
        TextView locView = (TextView)view.findViewById(R.id.label_location);
        locView.setText(movement.getPlaceName() + " at " + getTimeString(movement.getTimeStamp()));
        ImageView icon = (ImageView)view.findViewById(R.id.icon_activity);
        icon.setImageResource(getIconId(activity));
        return view;
    }

    private int getIconId(String activity) {
        switch (activity) {
            case "Travelling":
                return R.drawable.travel;
            case "Cycling":
                return R.drawable.cycle;
            case "Walking":
                return R.drawable.walk;
            case "Running":
                return R.drawable.run;
            case "Standing Still":
                return R.drawable.stand;
            case "On Foot":
                return R.drawable.walk;
            default:
                return R.drawable.unknown;
        }
    }

    private LayoutInflater getInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private String getTimeString(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }

}
