package com.example.appagenda.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompromissoDao {
    @Insert
    void insert(com.example.appagenda.model.Compromisso compromisso);

    @Query("SELECT * FROM Compromisso")
    List<com.example.appagenda.model.Compromisso> getAll();

    @Query("SELECT * FROM Compromisso WHERE data = :data ORDER BY hora ASC")
    List<com.example.appagenda.model.Compromisso> getByDate(String data);

    @Query("DELETE FROM Compromisso")
    void deleteAll();
}
