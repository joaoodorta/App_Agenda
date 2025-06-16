package com.example.appagenda;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class FragmentoDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireContext(), this, ano, mes, dia);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d("DataEscolhida", "Ano: " + year + ", Mês: " + (month + 1) + ", Dia: " + day);
        Toast.makeText(getContext(), "Data: " + day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
    }
}
