package com.example.asteroides;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PuntuacionEntity.class}, version = 1)
public abstract class PuntuacionDatabase extends RoomDatabase {
    public abstract PuntuacionDao puntuacionDao();
}
