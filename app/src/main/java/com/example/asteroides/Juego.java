package com.example.asteroides;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Juego extends AppCompatActivity {
    private VistaJuego vistaJuego;
    public TextView textViewPuntuacion;
    private ImageView imageViewJuego1, imageViewJuego2, imageViewJuego3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_juego);

         vistaJuego =(VistaJuego) findViewById(R.id.vistaJuego);
         textViewPuntuacion = (TextView)findViewById(R.id.puntuacion);
         imageViewJuego1 = (ImageView)findViewById(R.id.logoJuego1);
         imageViewJuego2 = (ImageView)findViewById(R.id.logoJuego2);
         imageViewJuego3 = (ImageView)findViewById(R.id.logoJuego3);
    }

    /**Cuando la actividad va hacer pausada detenemos todos los audios que se esten reproduciendo y pausamos el
     * hilo de ejecución de la clase VistaJuego*/
    @Override
    protected void onPause() {
        super.onPause();
        vistaJuego.audio.soundPool.autoPause();
        vistaJuego.getHiloJuego().pausarHilo();
    }

    /**Cuando reanudamos la actividad, reproducimos los audios que quedaron en pausa y reanudamos el hilo
     * de ejecución de la clase VistaJuego*/
    @Override
    protected void onResume() {
        super.onResume();
      /* try {
            vistaJuego.getHiloJuego().sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        vistaJuego.audio.soundPool.autoResume();
        vistaJuego.getHiloJuego().reanudarHilo();
    }

    /**Cuando la actividad va a ser destruida, pausamos todos los audios en reproducción, destruimos el soundoPool que
     * controla el audio, admeás de detener el hilo de ejecución de la clase VistaJuego*/
    @Override
    protected void onDestroy() {
        vistaJuego.audio.soundPool.autoPause();
        vistaJuego.audio.soundPool.release();
        vistaJuego.getHiloJuego().detenerHilo();
        super.onDestroy();
    }

/*
    @Override
    protected void onSaveInstanceState(@NonNull Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        int puntaje = vistaJuego.getPuntaje();
        int numeroJuegos = vistaJuego.getNumeroJuegos();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle estadoGuardado) {
        super.onRestoreInstanceState(estadoGuardado);
         puntaje = estadoGuardado.getInt("puntaje");
         numeroJuegos = estadoGuardado.getInt("numeroJuegos");
    }*/
}