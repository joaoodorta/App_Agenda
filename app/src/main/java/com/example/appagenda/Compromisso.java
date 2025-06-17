package com.example.appagenda.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Compromisso {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String data;
    public String hora;
    public String descricao;

    public Compromisso(String data, String hora, String descricao) {
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
    }
}