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
    private Drawable drawableMisil;
    private Vector<Grafico> misiles; //Vector en donde se almacenan los disparos
    private Vector<Integer> tiempoMisiles; //Vector en donde se almacenarán los tiempos de vida de cada disparo
    private static int PASO_VELOCIDAD_MISIL = 12; //Módulo de velocidad constante del misil
    private boolean disparo = false; //Boleano que determina si el usario toco y solto la pantalla, significando un disparo
    //Hilo de ejecución
    private HiloEjecucion hiloJuego = new HiloEjecucion(); //Hilo de ejecución que permite el movimiento de los drawables
    private static int PERIODO_PROCESO = 50; //Periodo en milisegundos en lo que vamos a esperar para aplicar movimiento a los asteroides
    private long ultimoProceso = 0; //Periodo en milisegundo cuando se ejecuto el último proceso
    private int retrasoMovimiento = 0; //Contador con el que vamos a esperar 5 ciclos completos para emparejar el tiempo de retraso de la pantalla con el movimiento de los asteroides

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

        //Creación del grafico nave e iniciamos ambos vectores para los disparos
        Drawable  drawableNave = context.getResources().getDrawable(R.drawable.nave);
        nave = new Grafico(this,drawableNave);
        drawableMisil = context.getResources().getDrawable(R.drawable.disparo);
        misiles = new Vector<Grafico>();
        tiempoMisiles = new Vector<Integer>();
    }

    /** Método que llama cuando sabemos el tamaño de la pantalla ha sido calculado*/
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);

        //Colocamos la nave en el centro de la pantalla
        nave.setCordenadaXcentro(ancho / 2);
        nave.setCordenadaYcentro(alto / 2);

        ultimoProceso = System.currentTimeMillis(); //Guardamos la última hora en milisegundos cuando se ejecuto el último proceso
        hiloJuego.start(); //Iniciamos el hilo de ejecución
        audio.reproducirAudioFondo(); //Reproducimos un audio en ciclo infinito, que funcionará como audio de fondo
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
        //Dibujamos el disparo, cuando nuestro vector tenga un elemento
        if (!misiles.isEmpty()) {
            for (int i = 0; i < misiles.size(); i++) {
                if (misiles.get(i) != null) {
                    misiles.get(i).dibujarGrafico(canvas);
                }
            }
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

    /** Función principal que se va a llamar recurrente con un hilo de ejecución, en el cual se va aplicar movimiento a todo el juego.
     * Como son a los asteroides, nave, disparos, a los dos joysticks (movimiento y giro), además de encargarse de verificar la posibles
     * colisiones entre la nave y los asteroides (perder un juego), el disparo a los asteroides (que genera putaje). */
    protected void actualizarMoviento(){
        //Guardamos la hora actual del sistema para compararla con último proceso
        long ahora = System.currentTimeMillis();
        //¿Pasaron más de 50 milisegundos desde que se ejecuto el hilo de ejecución?
        if (ultimoProceso + PERIODO_PROCESO > ahora){
            return; //Si no ha pasado un tiempo de 50 milisegundos, volvemos a ejecutar el está misma función
        }

        //Calculamos cuantas veces han pasado 50 milisengundos (nuestro periodo proceso) desde la primera vez que se ejecuto y a eso lo llamamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; //Nuestro último proceso es el que acabamos de ejecutar

        //Si el usuario esta pulsando sobre algún de los joystick, se actualiza la posición del círculo interior del joystick
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

        /*El contador (retraso movimiento) funciona para retrasar el movimiento de los asteroides por 5 ciclos completos, ya que la pantalla tiene un
        retraso para mostrar el juego. Lo que podrá generar perder o tener el asteroide muy cerca de la nave, dejando al jugador
        sin oportunidad de hacer algo al respeto. Esto aplica también cuando nos salimos de la aplicación, para que al reanudar
        el movimiento de los asteroides sea sincronizado con el retraso de la pantalla */
        if(retrasoMovimiento > 5){
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
                    retrasoMovimiento = 0; //Al reiniciar el juego, se debe de retrasar 5 ciclos completos el movimiento de los asteroides
                    hiloJuego.pausarHilo();
                    reiniciarJuego(); //Llamamos al método reiniciar juego
                    break; //Salimos del ciclo, por que hemos perdido

                }
            }
        }
        //Si existe al menos un elemento en el vector misiles, vamos a actualizar su movimiento
        if(!misiles.isEmpty()){
            //Generamos un cilo para que recorra todos los misiles
            for (int i = 0; i < misiles.size(); i++){
                misiles.get(i).incrementaPosicion(retardo); //Aumentamos la posición del misil
                tiempoMisiles.set(i, tiempoMisiles.get(i) - (int) retardo); //Al tiempo de vida del misil, le restamos el retardo
                //Si el tiempo de vida es menor a 0, eliminamos el gráfico misil y su respectivo tiempo de vida
                if (tiempoMisiles.get(i) < 0){
                    misiles.remove(i);
                    tiempoMisiles.remove(i);
                    //Como el misil está activo, verificamos si hay alguna colisión con algún asteroide
                }else{
                    for (int j = 0; j < asteroides.size(); j++){
                        if(misiles.get(i).verificarColision(asteroides.elementAt(j))){
                            destruyeAsteroide(j); //Llamamos a la función destruir asteroide y desaparecemos el misil con su respectivo tiempo de vida
                            misiles.remove(i);
                            tiempoMisiles.remove(i);
                            break; //Salimos del ciclo, por que eliminaos al asteroide
                        }
                    }
                }

            }
        }

        retrasoMovimiento++; //Aumentamos el contador como retraso del movimiento de asteroides
    }

    /** Una vez que un asteroide realiza una colisión con la nave, reiniciamos el juego por completo. Como al principio
     * del mismo, pero restando una juego a los tres que tenemos al iniciar el juego y si ya no tenemos nos muestra el
     * fin del juego (game over).*/
    private void reiniciarJuego() {
        //Eliminamos los misiles que se encuentren activos, así como con su respectivo tiempo de vida
        if(!misiles.isEmpty()){
            misiles.clear();
            tiempoMisiles.clear();
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

    /** Función la cual destruye el asteroide que ha sido alcanzado por el misil, tenemos dos casos en especifico. El primero
     * que es cuando el asteroide a eliminar es grande, lo que generará una fragmentación de ese asteroide, lo que significa que
     * vamos a crear de 1 a 3 asteroides pequeños (de la misma forma al grande). Y el segundo caso en el cual el asteroide a
     * eliminar es pequeño, si su posición en el vector es mayor a los 5 asteroides que creamos desde el principio, no se creará
     * ninguno nuevo, pero si se encuntra en las primeras 5 posiciones del vector. Se creará uno nuevo, lo que permitirá tener al
     * menos 5 asteroides en el juego todo el juego*/
    private void destruyeAsteroide(int i) {
        synchronized (asteroides) {
            //¿El asteroide que vamos a eliminar es un asteroide grande?
            int posicionAsteroide;
            //Guardamos la posición del asteroide en versión fragmento (pequeño) y llamamos a la función fragmentar asteroide
            if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[0].getConstantState()) {
                posicionAsteroide = 1;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[2].getConstantState()) {
                posicionAsteroide = 3;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else if (asteroides.get(i).getImagen().getConstantState() == drawableAsteroide[4].getConstantState()) {
                posicionAsteroide = 5;
                asteroids.fragmentarAsteroide(asteroides, i, numeroFragmentos, posicionAsteroide);
            } else { //Entonces el asteroide a eliminar es pequeño, así que lo eliminamos y reproducimos el audio de explosión
                if (i <= 4) { //¿El asteroide que eliminamos se encuentra dentro de las primeras 5 posiciones del vector?
                    //Creamos un nuevo asteroide y lo colocamos en la posición del vector, lo que siempre permitirá tener al menos 5 asteroides en el juego
                    double incrementoX = asteroides.get(i).getIncrementoX();
                    double incrementoY = asteroides.get(i).getIncrementoY();
                    asteroides.remove(i);
                    asteroids.creasAsteroideEnPosicion(asteroides, i, incrementoX, incrementoY);
                }else{
                    asteroides.remove(i);
                }
                audio.reproducirExplosionAsteroidePequeno();

            }
        }

    }
    /** Creamos un misil nuevo, y un tiempo de vida que va a acompañar a ese misil, una vez que el tiempo del misil llego a 0.
     * Se eliminará en automatico el misil, y reproducimos el audio de disparo.*/
    private void activaMisil() {
        //Creamos un nuevo misil con la cordenada del centro de la nave
        Grafico misil = new Grafico(this, drawableMisil);
        misil.setCordenadaXcentro(nave.getCordenadaXcentro());
        misil.setCordenadaYcentro(nave.getCordenadaYcentro());
        misil.setAngulo(nave.getAngulo());
        misil.setIncrementoX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncrementoY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misiles.add(misil); //Agrego el Grafico misil al vector

        //Calculamos el tiempo de vida del misil
        int tiempoMisil = (int) (Math.min(this.getWidth() / Math.abs(misil.getIncrementoX()) , this.getHeight() / Math.abs(misil.getIncrementoY())) - 2);
        tiempoMisiles.add(tiempoMisil); //Agrego ese tiempo al vector tiempo misiles

        audio.reproducirDisparo(); //Reproducimos el audio de disparo
    }

    /** Hilo de ejecución que va a estar llamando recurrentemente a la función de actualizar movimiento, lo que generará
     * todo el movimiento al juego, este hilo de ejecución solo se detendrá cuando el usuario salga de la aplicación ya que si no
     * hacemos esto el juego seguirá consumiedo recursos del teléfono. Una vez que el usuario regrese a la aplicación se volvera
     * a reanudar el mismo, continuando con el juego.*/
    class HiloEjecucion extends Thread{
        boolean pausa = false, ejecutandose = false;
        @Override
        public void run() {
            ejecutandose = true;
            while (ejecutandose) {
                actualizarMoviento();
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
    /*Geter*/
    public HiloEjecucion getHiloJuego() {
        return hiloJuego;
    }

    public void setContador(int contador) {
        this.retrasoMovimiento = contador;
    }
}
