package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

public class VistaJuego extends View {
    private final Joystick joystick; //Clase con la que controlamos la nave
    PosicionamientoJoystick posicionamiento;
    private Asteroides asteroids; //Clase con la que llenamos los asteroides
    //Asteroides
    private Vector<Grafico> asteroides;
    private int numeroAsteroides = 5;  //Número de asteroides que se van a mostrar en el juego
    private int numeroFragmentos = 3;  //Número de fragmentos que se va a dividir cuando sea destruido
    private Drawable drawableAsteroide[] = new Drawable[6];
    //Nave
    private Grafico nave;
    private int giroNave; //Se determina con movimiento horizontal de la pantalla
    private static final int MAX_VELOCIDAD_NAVE = 20;
    //Misil
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo = false; //Bandera que determina si el misil se encuentra en movimiento
    private boolean disparo = false; //Bandera que determina si el usario toco y solto la pantalla, significando un disparo
    private int tiempoMisil; //Determina el tiempo que va a estar achivo en el juego
    //Hilo de ejecución
    private  HiloJuego thread = new HiloJuego(); //Hilo de ejecución que permite el movimiento de los drawables
    private static int PERIODO_PROCESO = 50; // **
    private long ultimoProceso = 0; // **
    //Movimiento nave
    private  float mX = 0, mY = 0; // **

    /** */
    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
        drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide1small);
        drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide2);
        drawableAsteroide[3] = context.getResources().getDrawable(R.drawable.asteroide2small);
        drawableAsteroide[4] = context.getResources().getDrawable(R.drawable.asteroide3);
        drawableAsteroide[5] = context.getResources().getDrawable(R.drawable.asteroide3small);

        Drawable drawableNave,drawableMisil; //Definición de drawables que vamos a dibujar

        //Clase con la que determinamos posicion y radios del joystick con respecto al tamaño de pantalla
        posicionamiento = new PosicionamientoJoystick(this);

        //Inicializamos la clase joystick pasando como parámetros (x,y,radioExterior,radioInterior)
        joystick = new Joystick(posicionamiento.getPosicionXjoystick(),posicionamiento.getPosicionYjoystick(),posicionamiento.getRadioExterior(),posicionamiento.getRadioInterior());

        //Creación del grafico nave y misil
        drawableNave = context.getResources().getDrawable(R.drawable.nave);
        nave = new Grafico(this,drawableNave);

        drawableMisil = context.getResources().getDrawable(R.drawable.disparo);
        misil = new Grafico(this, drawableMisil);

        //Creación de asteroides
        asteroides = new Vector<Grafico>(); //Inicializando el vector que contendrá los asteroides

        asteroids = new Asteroides(this,context,numeroAsteroides);
        asteroides = asteroids.getAsteroides(); //Obtenemos el arreglo lleno de asteroides

    }

    /** */
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);

        int[] cordenadas;
        int i = 0;

        //Colocamos la nave en el centro de la pantalla
        nave.setCordenadaXcentro((int) (ancho / 2) );
        nave.setCordenadaYcentro((int) (alto / 2) );

        asteroids.llenarArreglo(numeroAsteroides, alto, ancho);

        cordenadas = asteroids.getCordenadas();
        //Colocación de los asteroides en cordenas (x,y) de manera aleatoria
        for (Grafico asteroide:asteroides){
           asteroide.setCordenadaXcentro(cordenadas[i]);
           i++;
           asteroide.setCordenadaYcentro(cordenadas[i]);
        }

        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    /** */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i < asteroides.size(); i++){
            if(asteroides.get(i) != null){
                asteroides.get(i).dibujarGrafico(canvas);
            }
        }

        nave.dibujarGrafico(canvas);
        joystick.draw(canvas);
        if (misilActivo){
            misil.dibujarGrafico(canvas);
        }
    }

    /** */
    protected void actualizaFisica(){

        joystick.update();

        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora){
            return;
        }

        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        nave.setAngulo((int) nave.getAngulo() + giroNave * retardo);

        double velocidadX = joystick.getActuadorX() * MAX_VELOCIDAD_NAVE;
        double velocidadY = joystick.getActuadorY() * MAX_VELOCIDAD_NAVE;
        nave.setIncrementoX(velocidadX);
        nave.setIncrementoY(velocidadY);
        nave.incrementaPosicion(retardo);

        for (Grafico asteroide: asteroides){
            asteroide.incrementaPosicion(retardo);
        }

        if (misilActivo){
            misil.incrementaPosicion(retardo);
            tiempoMisil -= retardo;
            if(tiempoMisil < 0 ){
                misilActivo = false;
            }else {
                for (int i = 0; i < asteroides.size(); i++){
                    if (misil.verificarColision(asteroides.elementAt(i))){
                        destruyeAsteroide(i);
                        break;
                    }
                }
            }
        }

    }

    private void destruyeAsteroide(int i) {
        synchronized (asteroides) {

            Drawable imagen = null; //Definimos un drawable en donde vamos a guardar el asteroide (versión mas paqueña) que utilizaremos como fragmento
            //¿El asteroide a eliminar es grande?
            if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[0].getConstantState() ||
                    asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[2].getConstantState() ||
                    asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[4].getConstantState()){
                //Guardamos el drawable del asteroide versión pequeña
                if(asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[0].getConstantState() ){
                    imagen = drawableAsteroide[1];
                }else if(asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[2].getConstantState()){
                    imagen = drawableAsteroide[3];
                }else if(asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[4].getConstantState()){
                    imagen = drawableAsteroide[5];
                }

                for (int n= 0; n < numeroFragmentos; n++){ //Entonces creamos los fragmentos del asteroide grande que acaba de ser destruido

                    Grafico asteroide = new Grafico(this, imagen); //Creamos un nuevo grafico (asteroide pequeño)
                    asteroide.setCordenadaXcentro(asteroides.get(i).getCordenadaXcentro()); //Le asignamos la posicion del centro (x,y) del asteroide grande
                    asteroide.setCordenadaYcentro(asteroides.get(i).getCordenadaYcentro());
                    asteroide.setIncrementoX(Math.random() * 7 - 2);
                    asteroide.setIncrementoY(Math.random() * 7 - 2);
                    asteroide.setAngulo((int) (Math.random() * 360));
                    asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
                    if (n == 2){  //¿Es el tercer asteroide que vamos a crear?
                        asteroides.remove(i); //Eliminamos el asteroide grande y creamos un asteroide pequeño en la posición anterior
                        asteroides.add(i, asteroide);
                    }else{
                        asteroides.add(asteroide); //Colocamos el asteroide al final del vector
                    }

                }

            }else{ //Entonces el asteroide es pequeño

                asteroides.remove(i); //eliminamos el asteroide
                if (i <= 4){ //¿El asteroide que eliminamos se encuentra dentro de las primeras 5 posiciones?
                    crearAsteroide(i, false); //Creamos un nuevo asteroide y lo colocamos en la posición en la que acabamos de eliminar
                }
            }
            misilActivo = false; //Desactivamos el disparo ya que impacto en un asteroide

        }
    }

    public void crearAsteroide(int indice, boolean bandera){

        Grafico asteroide = new Grafico(this, drawableAsteroide[(int) (Math.random() * 5) + 1] );
        asteroide.setCordenadaXcentro((int) (Math.random() * posicionamiento.getAnchoPantalla()) );
        asteroide.setCordenadaYcentro((int) (Math.random()) * posicionamiento.getAltoPantalla() );
        asteroide.setIncrementoX(Math.random() * 7 - 2);
        asteroide.setIncrementoY(Math.random() * 7 - 2);
        asteroide.setAngulo((int) (Math.random() * 360));
        asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
        if (bandera == true){
            asteroides.add(indice,asteroide);
        }else{
            asteroides.add(asteroide);
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
                if (joystick.esPresionado((double) event.getX(), (double) event.getY())){
                    joystick.setPresionado(true);
                }else{
                    disparo = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                disparo = false;

                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if(joystick.getPresionado()) {
                    joystick.setActuador((double) event.getX(), (double) event.getY());
                    giroNave = 0;

                }else if(dy < 6 && dx > 6){
                    giroNave = Math.round((x - mX) / 2);
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                joystick.setPresionado(false);
                joystick.reiniciarActuador();
                if (disparo){
                    activaMisil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    private void activaMisil() {
        misil.setCordenadaXcentro(nave.getCordenadaXcentro() + (nave.getAncho() / 2) + 5 );
        misil.setCordenadaYcentro(nave.getCordenadaYcentro() + 5);
        misil.setAngulo(nave.getAngulo());
        misil.setIncrementoX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncrementoY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncrementoX()) , this.getHeight() / Math.abs(misil.getIncrementoY())) - 2;
        misilActivo = true;
    }
}
