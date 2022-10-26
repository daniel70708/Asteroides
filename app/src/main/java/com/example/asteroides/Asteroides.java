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

    public Asteroides(View view, Context context, int numeroAsteroides) {
        this.view = view;
        this.context = context;
        this.numeroAsteroides = numeroAsteroides;
        audio = new Audio(context);
        posicionamiento = new PosicionamientoJoystick(view);
        this.anchoPantalla = posicionamiento.getAnchoPantalla();
        this.altoPantalla = posicionamiento.getAltoPantalla();
        llenarArregloDrawable();
    }

    public Vector<Grafico> llenarArregloAsteroides(View view, Vector<Grafico> asteroides, int numeroAsteroides) {
        int[] cordenadasXY= new int[2];
        //Creamos un ciclo en donde se crearán los 5 asteroides
        int numeroAleatorio;
        for (int i = 0; i < numeroAsteroides; i++) {
            numeroAleatorio = (int) (Math.random() * 5);
            Grafico asteroide = new Grafico(view, drawableAsteroides[numeroAleatorio]); //Creamos el gráfico de acuerdo con el drawable anterior

            llenarCordenadasXY(cordenadasXY);
            asteroide.setCordenadaXcentro(cordenadasXY[0]);
            asteroide.setCordenadaYcentro(cordenadasXY[1]);

            //Agreamos incremento de velocidad en (x,y), angulo y velocidad de rotacion de manera aleatoria
            asteroide.setIncrementoX((int) ((Math.random() * 5) - 1));
            asteroide.setIncrementoY((int) ((Math.random() * 5) - 1));
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setVelocidadRotacion((int) ((Math.random() * 8) - 4));
            asteroides.add(asteroide); //Agregamos el asteroide al vector asteroides
        }
        return asteroides;
    }

    public void llenarArregloDrawable() {
        drawableAsteroides[0] = context.getResources().getDrawable(R.drawable.asteroide1);
        drawableAsteroides[1] = context.getResources().getDrawable(R.drawable.asteroide1small);
        drawableAsteroides[2] = context.getResources().getDrawable(R.drawable.asteroide2);
        drawableAsteroides[3] = context.getResources().getDrawable(R.drawable.asteroide2small);
        drawableAsteroides[4] = context.getResources().getDrawable(R.drawable.asteroide3);
        drawableAsteroides[5] = context.getResources().getDrawable(R.drawable.asteroide3small);
    }

    public int[] llenarCordenadasXY(int[] cordenadaXY) {
        switch ((int) ((Math.random() * 4) + 1)) {
            case 1:
                cordenadaXY[0] = -200; //-200
                cordenadaXY[1] = (int) (Math.random() * altoPantalla);
                break;
            case 2:
                cordenadaXY[0] = anchoPantalla + 200; //+200
                cordenadaXY[1] = (int) (Math.random() * altoPantalla);
                break;
            case 3:
                cordenadaXY[0] = (int) (Math.random() * anchoPantalla);
                cordenadaXY[1] = -200;//-200
                break;
            case 4:
                cordenadaXY[0] = (int) (Math.random() * anchoPantalla);
                cordenadaXY[1]= altoPantalla + 200;//+200
                break;
        }
        return cordenadaXY;
    }

    public Vector<Grafico> fragmentarAsteroide(Vector<Grafico> asteroides, int indice, int numeroFragmentos, int posicionAsteroide){
        int posicionXcentro,posicionYcentro;
        posicionXcentro = asteroides.get(indice).getCordenadaXcentro();
        posicionYcentro = asteroides.get(indice).getCordenadaYcentro();

        for (int i = 0; i < numeroFragmentos; i++){

            Grafico asteroide = new Grafico(view, drawableAsteroides[posicionAsteroide]); //Creamos un nuevo grafico (asteroide pequeño)
            asteroide.setCordenadaXcentro((int) posicionXcentro); //Le asignamos la posicion del centro (x,y) del asteroide grande
            asteroide.setCordenadaYcentro((int)  posicionYcentro);
            asteroide.setIncrementoX(Math.random() * 7 - 2);
            asteroide.setIncrementoY(Math.random() * 7 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
            if(i == 0){
                asteroides.remove(indice); //Eliminamos el asteroide grande y creamos un asteroide pequeño en la posición anterior
                audio.reproducirExplosionAsteroideGrande();
                asteroides.add(indice, asteroide);
            }else{
                asteroides.add(asteroide);
            }
        }
        return asteroides;
    }

    public Vector<Grafico> creasAsteroideEnPosicion(Vector<Grafico> asteroides, int indice){
        Grafico asteroide = new Grafico(view, drawableAsteroides[(int) (Math.random() * 5) + 1] );
        asteroide.setCordenadaXcentro((int) (Math.random() * posicionamiento.getAnchoPantalla()) );
        asteroide.setCordenadaYcentro((int) (Math.random()) * posicionamiento.getAltoPantalla() );
        asteroide.setIncrementoX(Math.random() * 7 - 2);
        asteroide.setIncrementoY(Math.random() * 7 - 2);
        asteroide.setAngulo((int) (Math.random() * 360));
        asteroide.setVelocidadRotacion((int) (Math.random() * 8 - 4));
        asteroides.add(indice, asteroide);
        return asteroides;
    }

    /**Obtener arreglo de drawables*/
    public Drawable[] getDrawableAsteroides() {
        return drawableAsteroides;
    }
}
