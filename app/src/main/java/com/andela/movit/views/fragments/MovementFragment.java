package com.andela.movit.views.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.andela.movit.R;
import com.andela.movit.views.adapters.MovementAdapter;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;
import com.andela.movit.data.DbRepo;
import com.andela.movit.views.dialogs.DateFragment;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

import java.util.ArrayList;
import java.util.Date;

public class MovementFragment extends ListFragment<Movement> {

    private View rootView;

    private Context context;

    private DbRepo repo;

    private Date queryDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movements, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rootView = view;
        context = getActivity();
        items = new ArrayList<>();
        initializeViews();
        queryDate = new Date();
        dbOperation = getQueryOperation();
        loadItems();
    }

    private void initializeViews() {
        listView = (ListView)rootView.findViewById(R.id.list_movements);
        adapter = new MovementAdapter(context, R.layout.movement_item, items);
        listView.setAdapter(adapter);
        noData = (TextView)rootView.findViewById(R.id.no_movement);
        setActivityTitle("Today");
    }

    private DbOperation getQueryOperation() {
        return new DbOperation() {
            @Override
            public DbResult execute() {
                repo = new DbRepo(context);
                return new DbResult(repo.getMovementsByDate(queryDate), null);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movements, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date:
                launchDatePicker();
                break;
            case android.R.id.home:
                getActivity().finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void setActivityTitle(String title) {
        getActivity().setTitle(title);
    }

    private void launchDatePicker() {
        DateFragment dateFragment = new DateFragment();
        dateFragment.setDateSetListener(dateListener);
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    private DatePickerDialog.OnDateSetListener dateListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date date = Utility.generateDate(year, monthOfYear, dayOfMonth);
            setActivityTitle(Utility.getDateString(date));
            queryDate = date;
            loadItems();
        }
    };

    @Override
    public void onPause() {
        closeDb();
        super.onPause();
    }

    private void closeDb() {
        if (repo != null) {
            repo.closeDatabase();
        }
    }
}
