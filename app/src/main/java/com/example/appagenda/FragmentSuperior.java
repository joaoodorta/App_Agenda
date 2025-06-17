package com.example.appagenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.appagenda.model.Compromisso;
import com.example.appagenda.model.AppDatabase;
import java.util.concurrent.Executors;

public class FragmentSuperior extends Fragment {
    private String dataSelecionada = "";
    private String horaSelecionada = "";

    public FragmentSuperior() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_superior, container, false);

        EditText campoDescricao = view.findViewById(R.id.campo_descricao);
        Button btnData = view.findViewById(R.id.btn_data);
        Button btnHora = view.findViewById(R.id.btn_hora);
        Button btnConfirmar = view.findViewById(R.id.btn_confirmar);

        btnData.setOnClickListener(v -> {
            new FragmentoDatePicker(data -> dataSelecionada = data).show(getParentFragmentManager(), "datePicker");
        });

        btnHora.setOnClickListener(v -> {
            new FragmentoTimePicker(hora -> horaSelecionada = hora).show(getParentFragmentManager(), "timePicker");
        });

        btnConfirmar.setOnClickListener(v -> {
            String descricao = campoDescricao.getText().toString();
            if (dataSelecionada.isEmpty() || horaSelecionada.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                Compromisso c = new Compromisso(dataSelecionada, horaSelecionada, descricao);
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase.getInstance(requireContext()).compromissoDao().insert(c);
                });
                Toast.makeText(getContext(), "Compromisso salvo!", Toast.LENGTH_SHORT).show();
                campoDescricao.setText(""); //limpar
                dataSelecionada = "";
                horaSelecionada = "";
            }
        });

        return view;
    }
}
