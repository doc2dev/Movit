package com.andela.movit.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.activities.VisitDetailsActivity;
import com.andela.movit.adapters.VisitAdapter;
import com.andela.movit.config.Constants;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;
import com.andela.movit.data.MovementRepo;
import com.andela.movit.models.Visit;

import java.util.ArrayList;

public class VisitFragment extends ListFragment<Visit> {

    private Context context;

    private View rootView;

    private MovementRepo repo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.rootView = view;
        this.context = getActivity();
        initializeViews();
    }

    private void initializeViews() {
        listView = (ListView)rootView.findViewById(R.id.list_location);
        noData = (TextView)rootView.findViewById(R.id.no_visits);
        items = new ArrayList<>();
        adapter = new VisitAdapter(context, R.layout.visit_item, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        dbOperation = queryOperation;
        loadItems();
    }

    private ListView.OnItemClickListener itemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Visit visit = adapter.getItem(position);
            Intent intent = new Intent(context, VisitDetailsActivity.class);
            intent.putExtra(Constants.CURRENT_VISIT.getValue(), visit.getPlaceName());
            context.startActivity(intent);
        }
    };

    private DbOperation queryOperation = new DbOperation() {
        @Override
        public DbResult execute() {
            repo = new MovementRepo(context);
            return new DbResult(repo.getVisits(), null);
        }
    };
}
