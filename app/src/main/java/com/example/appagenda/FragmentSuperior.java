package com.example.appagenda;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;

public class FragmentSuperior extends Fragment {
    public FragmentSuperior() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_superior, container, false);

        Button botaoData = view.findViewById(R.id.btn_data);
        botaoData.setOnClickListener(v -> {
            DialogFragment datePicker = new FragmentoDatePicker();
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        Button botaoHora = view.findViewById(R.id.btn_hora);
        botaoHora.setOnClickListener(v -> {
            DialogFragment timePicker = new FragmentoTimePicker();
            timePicker.show(getParentFragmentManager(), "timePicker");
        });

        return view;
    }
}