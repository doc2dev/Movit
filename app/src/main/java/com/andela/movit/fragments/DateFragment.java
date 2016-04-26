package com.andela.movit.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

import com.andela.movit.utilities.Utility;

import java.util.Date;

import static android.app.DatePickerDialog.*;

public class DateFragment extends DialogFragment {

    private OnDateSetListener dateSetListener;

    public void setDateSetListener(OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = new Date();
        int[] dateValues = Utility.getDateValues(date);
        return new DatePickerDialog(
                getActivity(),
                dateSetListener,
                dateValues[0],
                dateValues[1],
                dateValues[2]);
    }
}
