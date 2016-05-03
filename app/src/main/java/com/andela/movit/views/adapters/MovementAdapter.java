package com.andela.movit.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.FrameworkUtils;
import com.andela.movit.utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovementAdapter extends FillableAdapter<Movement> {

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public MovementAdapter(Context context, int resource, List<Movement> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = FrameworkUtils.getInflater(context).inflate(viewId, null);
        Movement movement = items.get(position);
        TextView actView = (TextView)view.findViewById(R.id.label_activity);
        String activity = movement.getActivityName();
        actView.setText(activity);
        TextView locView = (TextView)view.findViewById(R.id.label_visit);
        locView.setText(getDescription(movement));
        ImageView icon = (ImageView)view.findViewById(R.id.icon_activity);
        icon.setImageResource(FrameworkUtils.getIconId(activity));
        return view;
    }

    private String getDescription(Movement movement) {
        return movement.getPlaceName()
                + ", for "
                + Utility.getDurationString(movement.getDuration());
    }
}
