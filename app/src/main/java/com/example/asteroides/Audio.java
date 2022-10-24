package com.example.asteroides;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Audio {
    Context context;
    SoundPool soundPool;
    int ciclo,disparo,explosionAsteroideGrande,explosionAsteroidePequeno,movimiento;

    public Audio(Context context) {
        this.context = context;
        iniciarAudios();
    }

    public void iniciarAudios(){
        soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        ciclo = soundPool.load(context,R.raw.ciclo,1);
        disparo = soundPool.load(context, R.raw.disparo,0);
        explosionAsteroideGrande = soundPool.load(context,R.raw.explosion1,0);
        explosionAsteroidePequeno = soundPool.load(context,R.raw.explosion2,0);
        movimiento = soundPool.load(context,R.raw.movimiento,0);
    }

    public void reproducirCiclo(){
        soundPool.play(ciclo,1,1,1,-1,1);
    }

    public void pausarCiclo(){
        soundPool.pause(ciclo);
    }

    public void reanudarCiclo(){
        soundPool.resume(ciclo);
    }

    public void reproducirDisparo(){
        soundPool.play(disparo,1,1,0,0,1);
    }

    public void reproducirExplosionAsteroideGrande(){
        soundPool.play(explosionAsteroideGrande,1,1,0,0,1);
    }

    public void reproducirExplosionAsteroidePequeno(){
        soundPool.play(explosionAsteroidePequeno,1,1,0,0,1);
    }

    public void reproducirMovimiento(){
        soundPool.play(movimiento,1,1,0,0,1);
    }

    public void pausarMovimiento(){
        soundPool.pause(movimiento);
    }

}
