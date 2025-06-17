package com.example.appagenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appagenda.model.Compromisso;
import com.example.appagenda.model.CompromissoDao;
import com.example.appagenda.model.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FragmentInferior extends Fragment {

    private TextView textoCompromissos;

    public FragmentInferior() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inferior, container, false);

        Button btnHoje = view.findViewById(R.id.btn_hoje);
        Button btnOutraData = view.findViewById(R.id.btn_outra_data);
        Button btnApagarTudo = view.findViewById(R.id.btn_apagar_tudo);
        btnApagarTudo.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(requireContext()).compromissoDao().deleteAll();

                requireActivity().runOnUiThread(() -> {
                    textoCompromissos.setText("Todos os compromissos foram apagados.");
                });
            });
        });

        textoCompromissos = view.findViewById(R.id.texto_compromissos);

        btnHoje.setOnClickListener(v -> carregarCompromissosDoDia(obterDataHoje()));

        btnOutraData.setOnClickListener(v -> {
            new FragmentoDatePicker(dataSelecionada -> carregarCompromissosDoDia(dataSelecionada))
                    .show(getParentFragmentManager(), "datePickerInferior");
        });

        // Carrega compromissos de hoje por padrão
        carregarCompromissosDoDia(obterDataHoje());

        return view;
    }

    private String obterDataHoje() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private void carregarCompromissosDoDia(String data) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Compromisso> lista = AppDatabase.getInstance(requireContext())
                    .compromissoDao()
                    .getByDate(data);

            StringBuilder sb = new StringBuilder();
            if (lista.isEmpty()) {
                sb.append("Nenhum compromisso para ").append(data);
            } else {
                for (Compromisso c : lista) {
                    sb.append(c.hora).append(": ").append(c.descricao).append("\n");
                }
            }

            requireActivity().runOnUiThread(() -> textoCompromissos.setText(sb.toString()));
        });
    }
}
