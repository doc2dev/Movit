package com.andela.movit.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.models.Visit;
import com.andela.movit.utilities.Utility;

import java.util.List;

public class VisitAdapter extends FillableAdapter<Visit> {

    public VisitAdapter(Context context, int resource, List<Visit> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = Utility.getInflater(context).inflate(viewId, null);
        Visit visit = items.get(position);
        TextView locationView = (TextView)view.findViewById(R.id.label_visit);
        locationView.setText(visit.getPlaceName());
        TextView dateView = (TextView)view.findViewById(R.id.label_duration);
        dateView.setText("Time spent: " + Utility.getDurationString(visit.getDuration()));
        return view;
    }
}
