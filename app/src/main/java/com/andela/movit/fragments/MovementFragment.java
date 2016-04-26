package com.andela.movit.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.andela.movit.adapters.MovementAdapter;
import com.andela.movit.background.DbAsync;
import com.andela.movit.data.DbCallback;
import com.andela.movit.data.DbOperation;
import com.andela.movit.data.DbResult;
import com.andela.movit.data.MovementRepo;
import com.andela.movit.models.Movement;
import com.andela.movit.utilities.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovementFragment extends Fragment {

    private ListView listView;

    private View rootView;

    private List<Movement> movements = new ArrayList<>();

    private Context context;

    MovementAdapter adapter;

    private TextView noMovements;

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
        initializeViews();
        loadMovementsByDate(new Date());
    }

    private void loadMovementsByDate(Date date) {
        DbAsync dbAsync = new DbAsync(getDbCallback());
        dbAsync.execute(getQueryOperation(date));
    }

    private DbOperation getQueryOperation(final Date date) {
        return new DbOperation() {
            @Override
            public DbResult execute() {
                MovementRepo repo = new MovementRepo(context);
                return new DbResult(repo.getMovementsByDate(date), null);
            }
        };
    }

    private DbCallback getDbCallback() {
        return new DbCallback() {
            @Override
            public void onOperationSuccess(Object result) {
                movements = (List<Movement>)result;
                fillAdapter();
            }

            @Override
            public void onOperationFail(String errorMessage) {
            }
        };
    }

    private void fillAdapter() {
        if (movements.size() > 0) {
            adapter.setMovements(movements);
            adapter.notifyDataSetChanged();
            toggleViews(true);
        } else {
            toggleViews(false);
        }
    }

    private void toggleViews(boolean movementsAvailable) {
        if (movementsAvailable) {
            listView.setVisibility(View.VISIBLE);
            noMovements.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noMovements.setVisibility(View.VISIBLE);
        }
    }

    private void initializeViews() {
        listView = (ListView)rootView.findViewById(R.id.list_movements);
        adapter = new MovementAdapter(context, R.layout.movement_item, movements);
        listView.setAdapter(adapter);
        noMovements = (TextView)rootView.findViewById(R.id.no_movement);
        setActivityTitle("Today");
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
            loadMovementsByDate(date);
        }
    };
}
