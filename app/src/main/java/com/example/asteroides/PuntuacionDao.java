package com.example.asteroides;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PuntuacionDao {

    @Query("SELECT * FROM PuntuacionEntity ORDER BY puntaje")
    List<PuntuacionEntity> getAll();

    @Query("SELECT COUNT(id_puntuacion) FROM PuntuacionEntity WHERE puntaje > :puntuacion")
    int buscarPorPuntaje(int puntuacion);

    @Insert
    void agregarPuntuacion(PuntuacionEntity puntuacion);

    @Update
    void actualizarPuntuacion(PuntuacionEntity puntuacion);

    @Delete
    void eliminarPuntuacion(PuntuacionEntity puntuacion);

}
