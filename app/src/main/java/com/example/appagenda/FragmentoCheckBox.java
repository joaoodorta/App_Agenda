package com.example.appagenda;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentoCheckBox extends Fragment {
    private CheckBox checkBox;

    @Nullable
    public View onCreateiew(@NonNull LayoutInflater Inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState){
        View view = Inflater.inflate(R.layout.fragment_checkbox,container,false);

        checkBox = view.findViewById(R.id.checkbox_example);

        checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) {
                Toast.makeText(getContext(), "Tarefa Feita", Toast.LENGTH_SHORT).show();
            }
        }));

        return view;

    }


}
