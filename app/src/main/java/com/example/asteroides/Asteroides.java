package com.example.asteroides;

import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.Vector;

public class Asteroides {

    private Audio audio;
    private Context context;
    private View view;
    private Drawable drawableAsteroides[] = new Drawable[6];
    private int numeroAsteroides;
    private PosicionamientoJoystick posicionamiento;
    private int anchoPantalla, altoPantalla;

    /**Clase que se enfoca en el llenado de asteroides en el vector de asteroides, desde el inicio del juego
     * así como cuando destruimos un asteroide grande y creamos asteroides más pequeños que conocemos como
     * fragmentos*/
    public Asteroides(View view, Context context, int numeroAsteroides) {
        this.view = view;
        this.context = context;
        this.numeroAsteroides = numeroAsteroides; //Número de asteroides que vamos a crear desde el inicio
        //Inicializamos la clase audio y la clase posicionamiento
        audio = new Audio(context);
        posicionamiento = new PosicionamientoJoystick(view);
        //Obtenemos el alto y ancho de la pantalla, que nos servirá para asignar la posición (x,y) de los asteroides
        this.anchoPantalla = posicionamiento.getAnchoPantalla();
        this.altoPantalla = posicionamiento.getAltoPantalla();
        llenarArregloDrawable();
    }

    /**Función con la que llenamos el arreglo asteroides, cuando recien inicia el juego*/
    public Vector<Grafico> llenarArregloAsteroides(View view, Vector<Grafico> asteroides, int numeroAsteroides) {
        int[] cordenadasXY= new int[2]; //Usamos este arreglo para la cordenada (x,y) del asteroide

        int numeroAleatorio;
        //Creamos un ciclo en donde se crearán los 5 asteroides
        for (int i = 0; i < numeroAsteroides; i++) {
            numeroAleatorio = (int) (Math.random() * 5);
            //Generamos de manera aleatoria el asteroide que vamos a crear (tenemos 6 opciones disponibles)
            Grafico asteroide = new Grafico(view, drawableAsteroides[numeroAleatorio]); //Creamos el gráfico de acuerdo con el drawable anterior
            //Llamamos a la función para llenar las cordenadas (x,y) del asteroide
            llenarCordenadasXY(cordenadasXY);
            asteroide.setCordenadaXcentro(cordenadasXY[0]);
            asteroide.setCordenadaYcentro(cordenadasXY[1]);

            //Agregamos las demás carácteristicas de manera aleatoria
            asteroide.setIncrementoX((int) ((Math.random() * 5) - 1));
            asteroide.setIncrementoY((int) ((Math.random() * 5) - 1));
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setVelocidadRotacion((int) ((Math.random() * 8) - 4));
            asteroides.add(asteroide); //Agregamos el asteroide al vector asteroides
        }
        //Regresamos el vector de graficos que recibimos como parámetro
        return asteroides;
    }

    /**Función en la cual ingresamos todos los asteroides disponibles (los tres asteroides disponibles
     * tienen una versión paqueña que funcionará como fragmento, que servirá cuando alcanzado por el disparo de
     * la nave)*/
    public void llenarArregloDrawable() {
        //El primer drawlabe es el asteroide grande seguido por su versión paqueña (fragmento)
        drawableAsteroides[0] = context.getResources().getDrawable(R.drawable.asteroide1);
        drawableAsteroides[1] = context.getResources().getDrawable(R.drawable.asteroide1small);
        drawableAsteroides[2] = context.getResources().getDrawable(R.drawable.asteroide2);
        drawableAsteroides[3] = context.getResources().getDrawable(R.drawable.asteroide2small);
        drawableAsteroides[4] = context.getResources().getDrawable(R.drawable.asteroide3);
        drawableAsteroides[5] = context.getResources().getDrawable(R.drawable.asteroide3small);
    }

    /**Función que determina la posición (x,y) que va a tener el asteroide, de 4 opciones posibles*/
    public int[] llenarCordenadasXY(int[] cordenadaXY) {
        //Generamos un número aleatoriamente para determinr de donde partirá nuestro asteroide
        switch ((int) ((Math.random() * 4) + 1)) {
            case 1:
                //La posición del asteroide empezará del lado izquiero de la pantalla
                cordenadaXY[0] = -200;
                cordenadaXY[1] = (int) (Math.random() * altoPantalla);
                break;
            case 2:
                //La posición del asteroide empezará del lado derecho de la pantalla
                cordenadaXY[0] = anchoPantalla + 200;
                cordenadaXY[1] = (int) (Math.random() * altoPantalla);
                break;
            case 3:
                //La posición del asteroide empezará de la parte de abajo de la pantalla
                cordenadaXY[0] = (int) (Math.random() * anchoPantalla);
                cordenadaXY[1] = -200;
                break;
            case 4:
                //La posición del asteroide empezará en la parte de ariba de la pantalla
                cordenadaXY[0] = (int) (Math.random() * anchoPantalla);
                cordenadaXY[1]= altoPantalla + 200;
                break;
        }
        return cordenadaXY; //Regresamos el arreglo para poder asignar el valor al asteroide
    }

    /**Función que crea los fragmentos (versión pequeña) del asteroide cuando es alcanzado por el disparo de la nave*/
    public Vector<Grafico> fragmentarAsteroide(Vector<Grafico> asteroides, int indice, int numeroFragmentos, int posicionAsteroide){
        int posicionXcentro,posicionYcentro;
        double incrementoX, incrementoY;
        //Guardamos las carácteristicas del asteroide grande que se usarán de referencia para la creación de los fragmentos
        posicionXcentro = asteroides.get(indice).getCordenadaXcentro();
        posicionYcentro = asteroides.get(indice).getCordenadaYcentro();
        incrementoX = asteroides.get(indice).getIncrementoX();
        incrementoY = asteroides.get(indice).getIncrementoY();

        //Creamos el ciclo de acuerdo al numéro de fragmentos que se dividirá el asteroide
        for (int i = 0; i < numeroFragmentos; i++){
            Grafico asteroide = new Grafico(view, drawableAsteroides[posicionAsteroide]); //Creamos un nuevo grafico (asteroide pequeño)
            asteroide.setCordenadaXcentro(posicionXcentro); //Le asignamos la posicion del centro (x,y) del asteroide grande
            asteroide.setCordenadaYcentro(posicionYcentro);
            //Incrementamos la velocidad del asteroide pequeño tomando como referencia al asteroide grande
            asteroide.setIncrementoX(Math.random() * 2 + incrementoX);
            asteroide.setIncrementoY(Math.random() * 2 + incrementoY);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
            //Creamos un fragmento en la posición del asteroide grande
            if(i == 0){
                asteroides.remove(indice); //Eliminamos el asteroide grande y creamos un asteroide pequeño en la posición anterior del vector
                audio.reproducirExplosionAsteroideGrande(); //Reproducimos el audio de explosión para el asteroide grande
                asteroides.add(indice, asteroide);
            }else{ //Si no es el primero en ser creado, este se va a ubiar en la parte final del vector
                asteroides.add(asteroide);
            }
        }
        return asteroides; //Regresamos el vector que recibimos como parámetro
    }

    /**Función que crea un asteroide, si este se encuentra en las primeras 5 posiciones del vector. Esto permite que sea
     * un ciclo infinito en donde siempre que eliminemos un asteroide cree uno nuevo*/
    public Vector<Grafico> creasAsteroideEnPosicion(Vector<Grafico> asteroides, int indice){
        int[] cordenadasXY= new int[2];
        double incrementoX = asteroides.get(indice).getIncrementoX();
        double incrementoY = asteroides.get(indice).getIncrementoY();

        //Creamos un asteroide de manera aleatoria pero se coloca en la posción en donde se elimino el asteroide anterior
        Grafico asteroide = new Grafico(view, drawableAsteroides[(int) (Math.random() * 5) + 1] );
        llenarCordenadasXY(cordenadasXY);
        asteroide.setCordenadaXcentro(cordenadasXY[0]);
        asteroide.setCordenadaYcentro(cordenadasXY[1]);
        asteroide.setIncrementoX(Math.random() * 2 + incrementoX);
        asteroide.setIncrementoY(Math.random() * 2 + incrementoY);
        asteroide.setAngulo((int) (Math.random() * 360));
        asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
        asteroides.add(indice, asteroide);
        return asteroides;//Regresamos el vector que recibimos como parámetro
    }

    /**Obtener arreglo de drawables*/
    public Drawable[] getDrawableAsteroides() {
        return drawableAsteroides;
    }
}
