package com.example.appagenda;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentoTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public interface OnTimeSelectedListener {
        void onTimeSelected(String hora);
    }

    private OnTimeSelectedListener listener;

    public FragmentoTimePicker(OnTimeSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), this,
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hora = String.format("%02d:%02d", hourOfDay, minute);
        listener.onTimeSelected(hora);
    }
}