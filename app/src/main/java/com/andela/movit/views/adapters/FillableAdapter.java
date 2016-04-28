package com.andela.movit.views.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class FillableAdapter<T> extends ArrayAdapter<T> {

    protected Context context;

    protected List<T> items;

    protected int viewId;

    public FillableAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.context = context;
        this.viewId = resource;
    }

    public void fillItems(List<T> incomingItems) {
        items.clear();
        for (T item : incomingItems) {
            items.add(item);
        }
    }
}
