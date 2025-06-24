package com.example.appagenda;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appagenda.model.Compromisso;
import com.example.appagenda.model.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FragmentInferior extends Fragment {

    private LinearLayout layoutCompromissos;

    public FragmentInferior() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inferior, container, false);

        // Referência aos componentes do layout
        Button btnHoje = view.findViewById(R.id.btn_hoje);
        Button btnOutraData = view.findViewById(R.id.btn_outra_data);
        Button btnApagarTudo = view.findViewById(R.id.btn_apagar_tudo);
        layoutCompromissos = view.findViewById(R.id.layout_compromissos);

        // Clique no botão "Hoje"
        btnHoje.setOnClickListener(v -> carregarCompromissosDoDia(obterDataHoje()));

        // Clique no botão "Outra Data"
        btnOutraData.setOnClickListener(v -> {
            new FragmentoDatePicker(dataSelecionada -> carregarCompromissosDoDia(dataSelecionada))
                    .show(getParentFragmentManager(), "datePickerInferior");
        });

        // Clique no botão "Apagar Tudo"
        btnApagarTudo.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(requireContext()).compromissoDao().deleteAll();

                requireActivity().runOnUiThread(() -> {
                    layoutCompromissos.removeAllViews();
                    TextView vazio = new TextView(getContext());
                    vazio.setTextColor(Color.WHITE);
                    vazio.setText("Todos os compromissos foram apagados.");
                    layoutCompromissos.addView(vazio);
                });
            });
        });

        // Carrega compromissos de hoje por padrão ao abrir
        carregarCompromissosDoDia(obterDataHoje());

        return view;
    }

    // Função para obter a data de hoje no formato dd/MM/yyyy
    private String obterDataHoje() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    // Função para carregar os compromissos de uma determinada data
    private void carregarCompromissosDoDia(String data) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Compromisso> lista = AppDatabase.getInstance(requireContext())
                        .compromissoDao()
                        .getByDate(data);

                requireActivity().runOnUiThread(() -> {
                    layoutCompromissos.removeAllViews();

                    if (lista.isEmpty()) {
                        TextView vazio = new TextView(getContext());
                        vazio.setTextColor(Color.WHITE);
                        vazio.setText("Nenhum compromisso para " + data);
                        layoutCompromissos.addView(vazio);
                    } else {
                        for (Compromisso c : lista) {
                            LinearLayout itemLayout = new LinearLayout(getContext());
                            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                            itemLayout.setPadding(0, 10, 0, 10);

                            CheckBox checkBox = new CheckBox(getContext());
                            checkBox.setTextColor(Color.WHITE);
                            checkBox.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

                            TextView texto = new TextView(getContext());
                            texto.setText(c.hora + ": " + c.descricao);
                            texto.setTextSize(18);
                            texto.setTextColor(Color.WHITE);
                            texto.setPadding(8, 0, 0, 8);

                            itemLayout.addView(checkBox);
                            itemLayout.addView(texto);

                            layoutCompromissos.addView(itemLayout);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                    layoutCompromissos.removeAllViews();
                    TextView erro = new TextView(getContext());
                    erro.setTextColor(Color.RED);
                    erro.setText("Erro ao carregar compromissos.");
                    layoutCompromissos.addView(erro);
                });
            }
        });
    }
}
