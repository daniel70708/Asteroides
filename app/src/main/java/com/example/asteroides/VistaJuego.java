package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

public class VistaJuego extends View {
    //Clases con la que nos comunicamos durante la ejecución del juego
    PosicionamientoJoystick posicionamiento; //Clase con la que calculamos el posicionamiento (x,y) de ambos joystick de acuerdo al tamaño de la pantalla
    JoystickMovimiento joystickMovimiento; //Clase con la que controlamos el movimiento de la nave
    JoystickGiro joystickGiro; //Clase con la que controlamos el giro de la nave
    Asteroides asteroids; //Clase con la que llenamos el vector de asteroides
    Audio audio; // Clase que controla el audio
    //Asteroides
    private Vector<Grafico> asteroides; //Vector en donde se almacenan los asteroides
    private Drawable drawableAsteroide[] = new Drawable[6]; //Arreglo en donde almacenamos todos los asteroides posibles
    private int numeroAsteroides = 5;  //Número de asteroides con los que incia el juego
    private int numeroFragmentos = 3;  //Número de fragmentos que se va a dividir el asteroide (grande) cuando sea destruido
    //Nave
    private Grafico nave;
    private int giroNave; //Determina el giro de la nave
    private static final int MAX_VELOCIDAD_NAVE = 20; //Velocidad máxima de la nave
    //Misil
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12; //**
    private boolean misilActivo = false; //Boleano que determina si el misil se encuentra en movimiento por la pantalla
    private boolean disparo = false; //Boleano que determina si el usario toco y solto la pantalla, significando un disparo
    private int tiempoMisil; //Determina el tiempo que va a estar activo en el juego
    //Hilo de ejecución
    private HiloEjecucion hiloJuego = new HiloEjecucion(); //Hilo de ejecución que permite el movimiento de los drawables
    private static int PERIODO_PROCESO = 50; //Periodo en milisegundos en lo que vamos a esperar para aplicar movimiento a los asteroides
    private long ultimoProceso = 0; //Periodo en milisegundo cuando se ejecuto el último proceso
    private int contador = 0;

    /** Constructor de la clase que incia 5 clases que necesitaremos durante la ejecución del juego, la primera es
     * PosicionamientoJoystick que se encarga de determinar la posisción (x,y) y radios de ambos joystick. Las
     * clases JoystickMovimeitno e JoystickGiro, controlan el movimiento y la otra el giro de la nave. La tercera
     * Asteroides se encarga de llenar el vector con asteroides y la última clase se encarga de controlar el audio
     * del juego (disparo, explosión, etc.)*/
    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //Inicializamos la clase Posicionamiento, joystick, joystickGiro,asteroids e audio
        posicionamiento = new PosicionamientoJoystick(this);
        //A las clases joystick le pasamos como parámetros: posicion (x,y), radio exterior e interior
        joystickMovimiento = new JoystickMovimiento(posicionamiento.getPosicionXjoystick(),posicionamiento.getPosicionYjoystick(),posicionamiento.getRadioExterior(),posicionamiento.getRadioInterior());
        joystickGiro = new JoystickGiro(posicionamiento.getPosicionXjoystickGiro(), posicionamiento.getPosicionYjoystick(), posicionamiento.getRadioExterior(), posicionamiento.getRadioInterior());

        //Iniciamos la clase asteroides y llenamos el vector con asteroides (5)
        asteroides = new Vector<Grafico>(); //Inicializando el vector que contendrá los asteroides
        asteroids = new Asteroides(this,context,numeroAsteroides);
        asteroids.llenarArregloAsteroides(this,asteroides,numeroAsteroides); //Llenamos el vector con 5 asteroides
        drawableAsteroide = asteroids.getDrawableAsteroides(); //Llenamos el arreglo de drawables
        audio = new Audio(context); //Iniciamos la clase audio

        //Creación del grafico nave y misil
        Drawable  drawableNave = context.getResources().getDrawable(R.drawable.nave);
        nave = new Grafico(this,drawableNave);
        Drawable drawableMisil = context.getResources().getDrawable(R.drawable.disparo);
        misil = new Grafico(this, drawableMisil);
    }

    /** Método que llama cuando sabemos el tamaño de la pantalla ha sido calculado*/
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);

        //Colocamos la nave en el centro de la pantalla
        nave.setCordenadaXcentro(ancho / 2);
        nave.setCordenadaYcentro(alto / 2);

        ultimoProceso = System.currentTimeMillis(); //**
        hiloJuego.start(); //Iniciamos el hilo de ejecución
        audio.reproducirAudioFondo(); //Reproducimos un audio en ciclo infinito, que funcionará como audio de fonde
    }

    /** Método que se encarga de dibujar la vista*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Dibujamos la nave y ambos joysticks
        nave.dibujarGrafico(canvas);
        joystickMovimiento.draw(canvas);
        joystickGiro.draw(canvas);
        //Creamos un ciclo para dibujar todos los asteroides que tenga nuestro vector
        for(int i = 0; i < asteroides.size(); i++){
            //Cuando eliminamos un asteroide, ese espacio en el vector estará vacio. Por lo que pregutamos si hay un asteroide para poder dibujar
            if(asteroides.get(i) != null){
                asteroides.get(i).dibujarGrafico(canvas);
            }
        }
        //Si el disparo de la nave se activa, tenemos que dibujar el disparo
        if (misilActivo){
            misil.dibujarGrafico(canvas);
        }
    }

    /** Método que controla la pantalla táctil del dispositivo, en el cual controlaremos 3 aspectos del juego, el
     * primero es controlar el disparo de la nave, el segundo es movimiento de la nave con la clase JoystickMovimiento
     * y la tercera que controla el giro de la nave con la clase JoystickGiro*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){

            //El usuario esta pulsando sobre la pantalla sin movimiento
            case MotionEvent.ACTION_DOWN:
                //¿La pulsación es sobre el joystickMovimiento?
                if (joystickMovimiento.esPresionado((double) event.getX(), (double) event.getY())){
                    //Cambiamos el estado de presionado por un verdadero
                    joystickMovimiento.setPresionado(true);
                    //¿La pulsación es sobre el joystickGiro?
                }else if(joystickGiro.esPresionado((double) event.getX(), (double) event.getY())){
                    //Cambiamos el estado de presionado por un verdadero
                    joystickGiro.setPresionado(true);
                }else{
                    //Si el toque no es en ningún joystick entonces activamos el disparo
                    disparo = true;
                }
                break;

                //El usuario esta moviendo la pantalla, después de tocarla
            case MotionEvent.ACTION_MOVE:
                //Desactivamos el disparo, ya que este solo se activa con tocar y soltar la pantalla
                disparo = false;
                //¿Estamos sobre el joystickMoviento?
                if(joystickMovimiento.getPresionado()) {
                    //Actualizamos los actuadores x,y, de acuerdo al moviento de la pantalla
                    joystickMovimiento.setActuador((double) event.getX(), (double) event.getY());

                    //Si no estamos sobre el joystickMoviento, entonces ¿Estamos sobre el joystickGiro?
                }else if(joystickGiro.getPresionado()){
                    //Actualizamos los actuadores x,y, de acuerdo al moviento de la pantalla
                    joystickGiro.setActuador((double) event.getX(), (double) event.getY());
                }
                break;

                //El usuario dejo de pulsar sobre la pantalla
            case MotionEvent.ACTION_UP:
                //A cada clase joysctick le pasamos un false como presionado y reinicamos los actuadores, ya que no hay movimiento
                giroNave = 0; //Reiniciamos el giro de la nave ya que no está girando
                joystickMovimiento.setPresionado(false);
                joystickGiro.setPresionado(false);
                joystickMovimiento.reiniciarActuador();
                joystickGiro.reiniciarActuador();
                //Activamos el disparo, ya que el usuario pulso y solto la pantalla
                if (disparo){
                    activaMisil();
                }
                break;
        }
        return true;
    }

    /** */
    protected void actualizaFisica(){
        //
        long ahora = System.currentTimeMillis();
        //¿Pasaron más de 50 milisegundos desde que se ejecuto el hilo de ejecución?
        if (ultimoProceso + PERIODO_PROCESO > ahora){
            return; //Si no han pasado regresa la función
        }

        //Calculamos cuantas veces han pasado desde la primera vez que se ejecuto y a eso lo llamamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; //Nuestro último proceso es el que acabamos de ejecutar

        //Si el usuario esta pulsando sobre alguno de los joystick, se actualiza la posición del círculo interior del joystick
        joystickMovimiento.update();
        joystickGiro.update();

        //Si el joystick movimiento esta pulsado actualizamos el incremento en X y en Y
        if(joystickMovimiento.getPresionado()){
            //Multiplicamos los actuadores por la velocidad máxima que puede alcanzar nuestra nave
            nave.setIncrementoX(joystickMovimiento.getActuadorX() * MAX_VELOCIDAD_NAVE);
            nave.setIncrementoY(joystickMovimiento.getActuadorY() * MAX_VELOCIDAD_NAVE);
            //Incrementamos la posición de la nave de acuerdo al incremento calculado anteriormente
            nave.incrementaPosicion(retardo);

            //Si el joystick giro esta pulsado, aumentamos el giro de la nave
        }else if(joystickGiro.getPresionado()){
            giroNave += (joystickGiro.getActuadorX() - joystickGiro.getActuadorY()) * 2;
            //Al ángulo que ya tiene la nave le vamos sumando el giro que cálculamos anteriormente
            nave.setAngulo((int) nave.getAngulo() + giroNave );
        }

        /*El contador funciona para retrasar el movimiento de los asteroides por 5 ciclos completos, ya que la pantalla tiene un
        retraso para mostrar el juego. Lo que podrá generar perder o tener el asteroide muy cerca de la nave, dejando al jugador
        sin oportunidad de hacer algo al respeto. Esto aplica también cuando nos salimos de la aplicación, para que al reanudar
        el movimiento de los asteroides sea sincronizado con el retraso de la pantalla */
        if(contador > 5){
            for (Grafico asteroide: asteroides){
                asteroide.incrementaPosicion(retardo); //Incrementamos la posicón de cada asteroide
                //Al aumentar la posición del asteroide, ¿Colisiona con la nave?
                if(asteroide.verificarColision(nave)){
                    //Cuando hay una colisión, retrasamos el hilo de ejecución y luego paramos la ejecución del mismo
                    try {
                        hiloJuego.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    contador = 0; //Al reiniciar el juego, se debe de retrasar 5 ciclos completos el movimiento de los asteroides
                    hiloJuego.pausarHilo();
                    reiniciarJuego(); //Llamamos al método reiniciar juego
                    break;

                }
            }
        }
        //Si el misil se encuentra en activo, aumentamos su posición en la pantalla (movimiento)
        if (misilActivo){
            misil.incrementaPosicion(retardo);
            //Este misil tendrá un ciclo de vida que ira decreciendo de acuerdo al retardo
            tiempoMisil -= retardo;
            if(tiempoMisil < 0 ){ //Si se acabo el tiempo de vida del misil, este va a desaparecer
                misilActivo = false;
            }else { //Si esta activo verifica si hay alguna colisión con un asteroide
                for (int i = 0; i < asteroides.size(); i++){
                    if (misil.verificarColision(asteroides.elementAt(i))){
                        destruyeAsteroide(i); //Llamamos a la función destruir asteroide y desaparecemos el misil
                        misilActivo = false;
                        break;
                    }
                }
            }
        }
        contador++; //Aumentamos el contador como retraso del movimiento de asteroides
    }


    private void reiniciarJuego() {
        //Si dejamos una misil activo lo eliminamos el juego
        if(misilActivo){
            misilActivo = false;
        }
        //Limpiamos el vector de asteroides para reiniciar el juego
        asteroides.clear();
        //Movemos la nave al centro de la pantalla
        nave.setCordenadaXcentro(posicionamiento.getAnchoPantalla() / 2);
        nave.setCordenadaYcentro(posicionamiento.getAltoPantalla() /2);
        //Llenamos de nuevo el vector asteroides como al principio del juego
        asteroids.llenarArregloAsteroides(this,asteroides,numeroAsteroides);
        //Reanudamos de nuevo el hilo de ejecución
        hiloJuego.reanudarHilo();
    }

    private void destruyeAsteroide(int i) {
        synchronized (asteroides) {
            //¿El asteroide que vamos a eliminar es un asteroide grande?
            int posicionAsteroide;
            //Guardamos la posición del asteroide en versión fragmento y llamamos a la función fragmentar asteroide
            if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[0].getConstantState()) {
                posicionAsteroide = 1;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[2].getConstantState()) {
                posicionAsteroide = 3;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[4].getConstantState()) {
                posicionAsteroide = 5;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else { //Entonces el asteroide a eliminar es pequeño, así que lo eliminamos y reproducimos el audio de explosión a asteroide pequeño o fragmentado
                asteroides.remove(i);
                audio.reproducirExplosionAsteroidePequeno();
                if (i <= 4) { //¿El asteroide que eliminamos se encuentra dentro de las primeras 5 posiciones?
                    asteroids.creasAsteroideEnPosicion(asteroides, i); //Creamos un nuevo asteroide y lo colocamos en la posición en la que acabamos de eliminar
                }
            }
        }

    }

    private void activaMisil() {
        misil.setCordenadaXcentro(nave.getCordenadaXcentro() + (nave.getAncho() / 2) + 5 );
        misil.setCordenadaYcentro(nave.getCordenadaYcentro() + 5);
        misil.setAngulo(nave.getAngulo());
        misil.setIncrementoX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncrementoY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncrementoX()) , this.getHeight() / Math.abs(misil.getIncrementoY())) - 2;
        misilActivo = true;
        audio.reproducirDisparo();
    }

    class HiloEjecucion extends Thread{
        boolean pausa = false, ejecutandose = false;
        @Override
        public void run() {
            ejecutandose = true;
            while (ejecutandose) {
                actualizaFisica();
                synchronized (hiloJuego){
                    while (pausa){
                        try {
                            hiloJuego.wait();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }
            }
        }

        public void pausarHilo(){
            synchronized (hiloJuego){
                pausa = true;
            }
        }
        public void reanudarHilo(){
            synchronized (hiloJuego){
                pausa = false;
                hiloJuego.notifyAll();
            }
        }

        public void detenerHilo(){
            ejecutandose = false;
            if (pausa) reanudarHilo();
        }
    }

    public HiloEjecucion getHiloJuego() {
        return hiloJuego;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
}
