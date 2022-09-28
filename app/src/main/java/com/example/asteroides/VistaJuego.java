package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.Console;
import java.util.Vector;

public class VistaJuego extends View {
    //Asteroides
    private Vector<Grafico> asteroides; //Vector en donde se van almacenar los asteroides con sus respectivas características
    private int numeroAsteroides = 5;  //Número de asteroides que se van a mostrar en el juego
    private int numeroFragmentos = 3;  //Número de fragmentos que se va a dividir cuando sea destruido
    //Nave
    private Grafico nave;
    private int giroNave;
    private double aceleracionNave;
    private static final int MAX_VELOCIDAD_NAVE = 20;
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    //Hilo de ejecución
    private  HiloJuego thread = new HiloJuego();
    private static int PERIODO_PROCESO = 50;
    private long ultimoProceso = 0;
    //Movimiento nave
    private  float mX = 0, mY = 0;
    private boolean disparo = false;



    /** */
    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide,drawableMisil; //Definición de imagenes que vamos a mostrar
        double numeroAleatorio, angulo, rotacion;

        //Creación de la nave sin angulo ni rotación
        drawableNave = context.getResources().getDrawable(R.drawable.nave2);
        nave = new Grafico(this,drawableNave);

        //Creación de asteroides
        asteroides = new Vector<Grafico>(); //Inicializando el vector que contendrá los 5 asteroides
        //Creamos un ciclo en donde se crearán los 5 asteroides
        for (int i = 0; i<numeroAsteroides; i++){
            //Generamos un número al azar para determinar que imagen del asteroide (de un total de 3 posibles)
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

            Grafico asteroide = new Grafico(this, drawableAsteroide); // Llamamos a la función grafico pasando como parámetro la vista y la imagen a representar
            //Generamos cordenada (x,y) para el incremento de la posición (el movimiento del asteroide)
            double cordenadaX = Math.random() * 4 - 2;
            double cordenadaY = Math.random() * 4 - 2;
            asteroide.setCordenadaXincremento(cordenadaX); //Asignamos (x,y) a cordenada incremento
            asteroide.setCordenadaYincremento(cordenadaY);
            //Generamos un número al azar para determinar el angulo y rotación al asteroide
            numeroAleatorio = Math.random();
            angulo = numeroAleatorio * 360;
            rotacion = numeroAleatorio * 8 -4;
            asteroide.setAngulo((int) angulo); //Asignamos el ángulo y la rotación
            asteroide.setRotacion((int) rotacion);
            asteroides.add(asteroide); //Agregamos el asteroide al vector asteroides
        }
    }

    /** */
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);

        double centroX, centroY, valorAleatorio;
        boolean banCaso1 = false, banCaso2 = false, banCaso3 = false, banCaso4 = false;
        double centroYanteriorCaso1 = 0, centroYanteriorCaso2 = 0, centroXanteriorCaso1 = 0, centroXanteriorCaso2 = 0;

        //Colocamos la nave en el centro de la pantalla
        centroX = ancho / 2; //Calculamos en centro de la pantalla (ancho)
        centroY = alto / 2; //Calculamos en centro de la pantalla (alto)
        nave.setCordenadaXcentro((int) centroX); //Asigamos las cordenadas (x,y) de donde se va a ubicar
        nave.setCordenadaYcentro((int) centroY);

        //Colocación de los asteroides en cordenas (x,y) de manera aleatoria
        for (Grafico asteroide:asteroides){
            centroX = 0;
            centroY = 0;

            valorAleatorio = (Math.random() * 3);
            switch ((int) valorAleatorio){
                case 1:
                    centroX = -200;
                    centroY =  Math.random() * alto;
                    if (banCaso1 == true){
                        centroY = verificaPosicion(centroY, centroYanteriorCaso1);
                    }
                    banCaso1 = true;
                    centroYanteriorCaso1 = centroY;
                    break;
                case 2:
                    centroX = ancho + 200;
                    centroY = Math.random() * alto;
                    if (banCaso2 == true){
                        centroY = verificaPosicion(centroY, centroYanteriorCaso2);
                    }
                    banCaso2 = true;
                    centroYanteriorCaso2 = centroY;
                    break;
                case 3:
                    centroX = Math.random() * ancho;
                    centroY = -200;
                    if (banCaso3 == true){
                        centroX = verificaPosicion(centroX, centroXanteriorCaso1);
                    }
                    banCaso3 = true;
                    centroXanteriorCaso1 = centroX;
                    break;
                default:
                    centroX = Math.random() * ancho;
                    centroY = alto + 200;
                    if (banCaso4 == true){
                        centroX = verificaPosicion(centroX, centroXanteriorCaso2);
                    }
                    banCaso4 = true;
                    centroXanteriorCaso2 = centroX;
                    break;
            }

           asteroide.setCordenadaXcentro((int) centroX);
           asteroide.setCordenadaYcentro((int) centroY);
        }

        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    protected double verificaPosicion(double centro, double centroAnterior){
        double valor;
        if (centro >= centroAnterior){
            valor = centro - centroAnterior;
        }else{
            valor = centroAnterior - centro;
        }

        if (valor <= 100){
            centro  = centro + 100;
        }
        return centro;

    }

    /** */
    synchronized
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide:asteroides){
            asteroide.dibujarGrafico(canvas);
        }
        nave.dibujarGrafico(canvas);

    }



    /** */
    synchronized
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
        if (Math.hypot(nIncX,nIncY) <= MAX_VELOCIDAD_NAVE){
            nave.setCordenadaXincremento(nIncX);
            nave.setCordenadaYincremento(nIncY);
        }
        nave.incrementaPosicion(retardo);

        for (Grafico asteroide: asteroides){
            asteroide.incrementaPosicion(retardo);
        }
    }

    /** */
    class HiloJuego extends Thread{
        @Override
        public void run() {
            while (true){
                actualizaFisica();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                }else  if (dx < 6 && dy > 6){
                    aceleracionNave = Math.round( (mY - y) / 25);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                /*if (disparo){
                    activaMisil();
                }*/
                break;
        }
        mX = x;
        mY = y;
        return true;
    }




}
