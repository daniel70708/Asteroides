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
        //nave
        drawableNave = context.getResources().getDrawable(R.drawable.nave2);
        nave = new Grafico(this,drawableNave);
        double numeroAleatorio = Math.random();
        double angulo = numeroAleatorio * 360;
        double rotacion = numeroAleatorio * 4 / 2;
        nave.setAngulo((int) angulo);
        nave.setRotacion((int) rotacion);

        //asteroides
        asteroides = new Vector<Grafico>();
        for (int i = 0; i<numeroAsteroides; i++){

           switch ((int) Math.round(Math.random() * 3)){
                case 0:
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide2);
                    break;
                case 1:
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide3);
                    break;
                default:
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide4);
                    break;
            }

            Grafico asteroide = new Grafico(this, drawableAsteroide);
            double cordenadaX = Math.random() * 4 -2;
            double cordenadaY = Math.random() * 4 -2;
            asteroide.setCordenadaXincremento(cordenadaX);
            asteroide.setCordenadaYincremento(cordenadaY);

            numeroAleatorio = Math.random();
            angulo = numeroAleatorio * 360;
            rotacion = numeroAleatorio * 8 -4;
            asteroide.setAngulo((int) angulo);
            asteroide.setRotacion((int) rotacion);
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
        double centroX, centroY, valorAleatorio;
        valorAleatorio = Math.random();

        for (Grafico asteroide:asteroides){
            centroX = valorAleatorio * ancho;
            centroY = valorAleatorio * alto;
            asteroide.setCordenadaXcentro((int) centroX);
            asteroide.setCordenadaYcentro((int) centroY);
            valorAleatorio = Math.random();
        }

        centroX = valorAleatorio * ancho;
        centroY = valorAleatorio * alto;
        nave.setCordenadaXcentro((int) centroX);
        nave.setCordenadaYcentro((int) centroY);
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
