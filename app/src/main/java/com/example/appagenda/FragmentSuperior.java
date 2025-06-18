package com.example.appagenda;

import static java.lang.System.currentTimeMillis;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import com.example.appagenda.model.Compromisso;
import com.example.appagenda.model.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FragmentSuperior extends Fragment {
    private String dataSelecionada = "";
    private String horaSelecionada = "";

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private void agendarNotificacao(String data, String hora, String descricao) {

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        long triggerAtMillis = 0;
        PendingIntent pendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(), "Ative permissões de alarme exato nas configurações", Toast.LENGTH_LONG).show();
                return;
            }
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date dataHora = sdf.parse(data + " " + hora);
            assert dataHora != null;
            triggerAtMillis = dataHora.getTime();

            Intent intent = new Intent(requireContext(), AlarmReceiver.class);
            intent.putExtra("titulo", "Compromisso agendado");
            intent.putExtra("descricao", descricao + "às " + hora);

            pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    (int) currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FragmentSuperior() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }



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
                agendarNotificacao(dataSelecionada,horaSelecionada,descricao);
                Toast.makeText(getContext(), "Compromisso salvo!", Toast.LENGTH_SHORT).show();
                campoDescricao.setText(""); //limpar
                dataSelecionada = "";
                horaSelecionada = "";
            }
        });

        return view;
    }
}
