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

        Button btnHoje = view.findViewById(R.id.btn_hoje);
        Button btnOutraData = view.findViewById(R.id.btn_outra_data);
        Button btnApagarTudo = view.findViewById(R.id.btn_apagar_tudo);
        btnApagarTudo.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(requireContext()).compromissoDao().deleteAll();

                requireActivity().runOnUiThread(() -> {
                    layoutCompromissos.removeAllViews();
                    TextView vazio = new TextView(getContext());
                    vazio.setTextColor(Color.WHITE);
                    vazio.setText("Todos os Compromissos foram apagados");
                    layoutCompromissos.addView(vazio);
                });
            });
        });

        layoutCompromissos = view.findViewById(R.id.layout_compromissos);

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

            /*StringBuilder sb = new StringBuilder();
            if (lista.isEmpty()) {
                sb.append("Nenhum compromisso para ").append(data);
            } else {
                for (Compromisso c : lista) {
                    sb.append(c.hora).append(": ").append(c.descricao).append("\n");
                }
            }*/

            requireActivity().runOnUiThread(() -> layoutCompromissos.removeAllViews());

            if(lista.isEmpty()){
                TextView vazio = new TextView(getContext());
                vazio.setTextColor(Color.WHITE);
                vazio.setText("Nenhum Compromisso para " + data);
                layoutCompromissos.addView(vazio);
            }
            else{
                for (Compromisso c : lista){
                    LinearLayout itemLayout = new LinearLayout(getContext());
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    itemLayout.setPadding(0,10,0,10);

                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setTextColor(Color.WHITE);
                    checkBox.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

                    TextView texto = new TextView(getContext());
                    texto.setText(c.hora + ": " + c.descricao);
                    texto.setTextSize(18);
                    texto.setTextColor(Color.WHITE);
                    texto.setPadding(8,0,0,8);

                    itemLayout.addView(checkBox);
                    itemLayout.addView(texto);

                    requireActivity().runOnUiThread(()->layoutCompromissos.addView(itemLayout));
                }
            }

        });
    }
}
