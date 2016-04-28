package com.andela.movit.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.views.adapters.MovementAdapter;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;
import com.andela.movit.data.MovementRepo;
import com.andela.movit.models.Movement;

import java.util.ArrayList;

import static com.andela.movit.config.Constants.*;

public class VisitDetailsFragment extends ListFragment<Movement> {

    private Context context;

    private String placeName;

    private MovementRepo repo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        placeName = getActivity().getIntent().getStringExtra(CURRENT_VISIT.getValue());
        getActivity().setTitle(placeName);
        context = getActivity();
        items = new ArrayList<>();
        initializeViews(view);
        dbOperation = getQueryOperation();
        loadItems();
    }

    private DbOperation getQueryOperation() {
        return new DbOperation() {
            @Override
            public DbResult execute() {
                repo = new MovementRepo(context);
                return new DbResult(repo.getMovementsByLocation(placeName), null);
            }
        };
    }

    private void initializeViews(View view) {
        listView = (ListView)view.findViewById(R.id.list_movements);
        adapter = new MovementAdapter(context, R.layout.movement_item, items);
        listView.setAdapter(adapter);
        noData = (TextView)view.findViewById(R.id.no_movement);
    }
}
