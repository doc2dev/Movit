package com.andela.movit.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.movit.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovementsActivityFragment extends Fragment {

    public MovementsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movements, container, false);
    }
}
