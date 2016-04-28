package com.andela.movit.views.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.andela.movit.views.adapters.FillableAdapter;
import com.andela.movit.background.DbAsync;
import com.andela.movit.data.DbCallback;
import com.andela.movit.data.DbOperation;

import java.util.List;

public class ListFragment<T> extends Fragment {

    protected ListView listView;

    protected TextView noData;

    protected List<T> items;

    protected FillableAdapter<T> adapter;

    protected DbOperation dbOperation;

    private DbCallback dbCallback = new DbCallback() {
        @Override
        public void onOperationSuccess(Object result) {
            items = (List<T>)result;
            fillAdapter();
        }

        @Override
        public void onOperationFail(String errorMessage) {
        }
    };

    private void fillAdapter() {
        if (items.size() > 0) {
            adapter.fillItems(items);
            adapter.notifyDataSetChanged();
            toggleViews(true);
        } else {
            toggleViews(false);
        }
    }

    private void toggleViews(boolean movementsAvailable) {
        if (movementsAvailable) {
            listView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    protected void loadItems() {
        DbAsync dbAsync = new DbAsync(dbCallback);
        dbAsync.execute(dbOperation);
    }
}
