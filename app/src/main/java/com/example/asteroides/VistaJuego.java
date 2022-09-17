package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

public class VistaJuego extends View {
    //Asteroides
    private Vector<Grafico> asteroides;
    private int numeroAsteroides = 5;
    private int numeroFragmentos = 3;
    //Nave
    private Grafico nave;
    private int giroNave;
    private double aceleracionNave;
    private static final int MAX_VELOCIDAD_NAVE = 20;
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    //Hilo de ejecución
    /*private  HiloJuego hiloJuego = new HiloJuego();
    private static int PERIODO_PROCESO = 50;
    private long ultimoProceso = 0;
*/


    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide,drawableMisil;

        drawableNave = context.getResources().getDrawable(R.drawable.nave);
        nave = new Grafico(this,drawableNave);
        nave.setAngulo(0);
        nave.setRotacion(70);

        drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i<numeroAsteroides; i++){
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setCordenadaYincremento(Math.random() * 4 -2);
            asteroide.setCordenadaXincremento(Math.random() * 4 - 2);

            asteroide.setAngulo((int) Math.random() * 360);
            asteroide.setRotacion((int) Math.random() * 8 - 4);
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
        int i = 50;
        for (Grafico asteroide:asteroides){
            //do {

            asteroide.setCordenadaXcentro(100);
            asteroide.setCordenadaYcentro(i);
            i = i + 50;
                //asteroide.setCordenadaXcentro((int) Math.random() * ancho);
                //asteroide.setCordenadaYcentro((int) Math.random() * alto);
            //}while (asteroide.distancia(nave) < (ancho + alto) / 5);

        }

        nave.setCordenadaXcentro(300);
        nave.setCordenadaYcentro(200);
        /*ultimoProceso = System.currentTimeMillis();
        hiloJuego.run();*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide:asteroides){
            asteroide.dibujarGrafico(canvas);
        }
        nave.dibujarGrafico(canvas);
    }
/*
    protected void actualizaFisica(){
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora){
            return;
        }
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        nave.setAngulo((int) nave.getAngulo() + giroNave * retardo);
        double nIncX = nave.getCordenadaXincremento() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getCordenadaYincremento() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        if (Math.hypot(nIncX,nIncY) <= aceleracionNave){
            nave.setCordenadaXincremento(nIncX);
            nave.setCordenadaYincremento(nIncY);
        }
        nave.incrementaPosicion(retardo);
        for (Grafico asteroide: asteroides){
            asteroide.incrementaPosicion(retardo);
        }
    }

    class HiloJuego extends Thread{
        @Override
        public void run() {
            while (true){
                actualizaFisica();
            }
        }
    }*/
}
