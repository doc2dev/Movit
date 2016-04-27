package com.andela.movit.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.andela.movit.R;

public class MinutePickerFragment extends DialogFragment {

    private Dialog dialog;

    private MinuteCallback callback;

    private NumberPicker numberPicker;

    public void setCallback(MinuteCallback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.setTitle("Select time before logging");
        dialog.setContentView(R.layout.number_picker);
        numberPicker = getPicker(dialog);
        Button button = (Button)dialog.findViewById(R.id.select_button);
        button.setOnClickListener(getListener());
        return dialog;
    }

    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMinutesSelected(numberPicker.getValue());
                dialog.cancel();
            }
        };
    }

    private NumberPicker getPicker(Dialog dialog) {
        NumberPicker numberPicker = (NumberPicker)dialog.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(59);
        numberPicker.setMinValue(1);
        numberPicker.setValue(5);
        return numberPicker;
    }
}
