package com.andela.movit.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.andela.movit.R;

public class SettingsFragment extends Fragment {

    private Context context;

    private EditText minuteDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        initViews(view);
    }

    private void initViews(View view) {
        minuteDisplay = (EditText)view.findViewById(R.id.minutes_display);
        minuteDisplay.setOnClickListener(getListener());
    }

    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMinuteDialog();
            }
        };
    }

    private void launchMinuteDialog() {
        MinutePickerFragment fragment = new MinutePickerFragment();
        fragment
    }
}
