package com.example.asteroides;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Audio {
    Context context;
    SoundPool soundPool;
    int audioFondo,disparo,explosionAsteroideGrande,explosionAsteroideSmall;

    /** Constructor en el cual recibimos el context y mandamos a llamar  a la clase soundpool
     * definiendo los audios que usamos en la clase VistaJuego que son el ciclo de juego, disparo,
     * explosion del asteroide grande como pequeño*/
    public Audio(Context context) {
        this.context = context;
        iniciarAudios();
    }

    /** Iniciamos el soundpool con el cual vamos a reproducir un máximo de 10 audios en simultaneo y cargamos
     * los audios de fondo,disparo,explosion de asteroide (grande y pequeño), sin ninguna prioridad para los
     * audios*/
    public void iniciarAudios(){
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC, 0);
        audioFondo = soundPool.load(context,R.raw.ciclo,0);
        disparo = soundPool.load(context, R.raw.disparo,0);
        explosionAsteroideGrande = soundPool.load(context,R.raw.explosion1,0);
        explosionAsteroideSmall = soundPool.load(context,R.raw.explosion2,0);
    }

    /** Reproducimos el Audio de fondo con un ciclo de reprodución infinito*/
    public void reproducirAudioFondo(){
        soundPool.play(audioFondo,1,1,0,-1,1);
    }

    /** Reproducimos los audios de disparo, explosion asteroide (grande y pequeño) solo una vez, cuando
     * el juego lo necesite*/
    public void reproducirDisparo(){
        soundPool.play(disparo,1,1,1,0,1);
    }

    public void reproducirExplosionAsteroideGrande(){
        soundPool.play(explosionAsteroideGrande,1,1,1,0,1);
    }

    public void reproducirExplosionAsteroidePequeno(){
        soundPool.play(explosionAsteroideSmall,1,1,1,0,1);
    }
}
