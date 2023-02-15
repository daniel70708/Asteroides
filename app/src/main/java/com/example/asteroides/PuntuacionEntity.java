package com.example.asteroides;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "PuntuacionEntity")
public class PuntuacionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id_puntuacion;
    public String nombre;
    public int puntaje;
    public Date fecha;
    @Ignore
    public int imagen;
}
