package com.example.asteroides;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.Vector;

public class Asteroides {

    private Context context;
    private View view;
    private Drawable drawableAsteroide;
    private Vector<Grafico> asteroides;
    private int numeroAsteroides;
    private int[] cordenadas;

    public Asteroides(View view, Context context, int numeroAsteroides) {
        this.view = view;
        this.context = context;
        this.numeroAsteroides = numeroAsteroides;
        asteroides = new Vector<Grafico>();

        llenarArregloAsteroides(view,numeroAsteroides);

    }

    public void llenarArregloAsteroides(View view, int numeroAsteroides){
        //Creamos un ciclo en donde se crearán los 5 asteroides
        for (int i = 0; i<numeroAsteroides; i++){
            seleccionarDrawable();
            Grafico asteroide = new Grafico(view, drawableAsteroide); //Creamos el gráfico de acuerdo con el drawable anterior
            //Agreamos incremento de velocidad en (x,y), angulo y velocidad de rotacion de manera aleatoria
            asteroide.setIncrementoX( (int) ((Math.random() * 5) - 1) );
            asteroide.setIncrementoY((int) ((Math.random() * 5) - 1) );
            asteroide.setAngulo((int) (Math.random() * 360) );
            asteroide.setVelocidadRotacion((int) ((Math.random() * 8) -4) );
            asteroides.add(asteroide); //Agregamos el asteroide al vector asteroides
        }

    }

    public void seleccionarDrawable(){
        //Generamos un número al azar para determinar que imagen del asteroide (de un total de 3 posibles)
        switch ((int) Math.round(Math.random() * 2)){
            case 1:
                //Generamos y redondeamos un número aleatorio y si es menor a 1 generamos el asteroide pequeño
                if (Math.round(Math.random()) < 1){
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1small);
                }else{
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
                }
                break;
            case 2:
                if (Math.round(Math.random()) < 1){
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide2small);
                }else{
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide2);
                }
                break;
            default:
                if (Math.round(Math.random()) < 1){
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide3small);
                }else{
                    drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide3);
                }
                break;
            }
    }

    public void llenarArreglo(int numeroAsteroides, int altoPantalla, int anchoPantalla){
        int j = 0, centroX, centroY;
        int numeroArreglo = numeroAsteroides * 2;
        int[] casos = new int[5];
        cordenadas = new int[numeroArreglo];

        for (int i = 0; i < numeroArreglo; i++){
            centroX = 0;
            centroY = 0;

            switch ((int) ((Math.random() * 4) + 1)){
                case 1:
                    centroX = 0; //-200
                    centroY = (int) (Math.random() * altoPantalla);
                    casos[j] = 1;
                    break;

                    case 2:
                        centroX = anchoPantalla; //+200
                        centroY = (int) (Math.random() * altoPantalla);
                        casos[j] = 1;
                    break;
                case 3:
                        centroX = (int) (Math.random() * anchoPantalla);
                        centroY = 0;//-200
                        casos[j] = 2;
                    break;
                case 4:
                        centroX = (int) (Math.random() * anchoPantalla);
                        centroY = altoPantalla;//+200
                        casos[j] = 2;
                    break;
            }

            cordenadas[i] = centroX;
            i++;
            cordenadas[i] = centroY;
            j++;
        }
        verificaPosicion(casos);

    }

    public void verificaPosicion(int[] casos){
        int resta,cordenadaAnterior, cordenadaSiguente;

        for(int i = 0; i < casos.length; i++){ //Recorre el primercaso
            for(int j = i + 1; j < numeroAsteroides; j++){ //Compara un casos con todos los démas
                    if (casos[i] == casos[j]){ //¿Mi caso es igual al siguiente?
                        int numeroCaso = casos[i]; //Guardo el número de caso
                        if (casos[i] == 1){ //¿Mi caso es el número 1?
                            if(i == 0){ //¿Mi caso es la primera coordenada?
                                cordenadaAnterior = cordenadas[i+1];
                            }else { //Mi caso es la segunda o más
                                cordenadaAnterior = cordenadas[j*2-1];
                            }
                            cordenadaSiguente = cordenadas[j*2+1];
                            resta = cordenadaAnterior - cordenadaSiguente;
                            if (Math.abs(resta) < 100){ //¿La cordenada es menor a 100?
                                cordenadas[j*2-1] = cordenadas[j*2-1] + 100;//Sumo 100 a la cordenada para separarla
                            }
                        }else{ // Mi caso es el número 2
                            if (i == 0){//¿Mi caso es la primera coordenada?
                                cordenadaAnterior = cordenadas[0];
                            }else { //Mi caso es la segunda o más
                                cordenadaAnterior = cordenadas[i*2];
                            }
                            cordenadaSiguente = cordenadas[j*2];
                            resta = cordenadaAnterior - cordenadaSiguente;
                            if (Math.abs(resta) < 100){//¿La cordenada es menor a 100?
                                cordenadas[i] = cordenadas[i] + 100;//Sumo 100 a la cordenada para separarla
                            }
                        }
                    }
                }
        }

    }

    public int[] getCordenadas() {
        return cordenadas;
    }

    public Vector<Grafico> getAsteroides() {
        return asteroides;
    }
}
