package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafico {
    private View view;
    private Drawable imagen; //Drawable que vamos a mostrar en nuestro juego (nave, asteroide ó disparo)
    private int cordenadaXcentro,cordenadaYcentro; //Cordenadas (x,y) del centro de nuestro drawable
    private int alto,ancho; //Alto y ancho del drawable
    private double incrementoX, incrementoY;// Incremento de velocidad en eje de X y de Y
    private double angulo, velocidadRotacion; //Angulo y velocidad de rotación del drawable
    private int radioColision; //Si un objeto se encuentra dentro de este radio, significa que tendremos una colision
    private int cordenadaXanterior,cordenadaYanterior; //Cordenanda (x,y) anterior del drawable
    private int radioInval; //Incrementamos el radio de la imagen porque será rotada y generará un conflicto con el rectangulo que definimos al principio (setBounds)

    /** Constructor de la clase, le pasamos como parámetros la vista y la imagen a ser dibujada*/
    public Grafico(View view, Drawable imagen){
        this.view = view;
        this.imagen = imagen;
        ancho = imagen.getIntrinsicWidth(); //Obtenemos en ancho de la imagen
        alto = imagen.getIntrinsicHeight();//Obtenemos el alto de la imagen
        radioColision =  ( (alto/2) + (ancho/2) ) / 2; //Calculamos en radio de colision
        radioInval = (int) Math.hypot(ancho / 2 , alto / 2); //Calculamos la distancia extra que necesitaremos para poder rotar la imagen sin tener que dibujarla de nuevo
    }

    /** Dibujamos el drawable que recibimos en el contructor de la clase, para hacer esto definimos un rectángulo en donde va a poder
     * ser dibujado, Una vez dibujadom vamos a rotar nuestro drawable, por lo que aumentaremos el tamaño de nuestro rectángulo evitando
     * que la imagen sobrepase nuestro rectángulo, para después indicarle a la vista que tiene que redibujar la imagen ya que fue
     * modificada y por último le indicamos a la vista que va a redibujar el drawable porque cambio de posición*/
    public void dibujarGrafico(Canvas canvas){
        //Calculamos la cordenada(x,y) de nuestro drawable que será en la esquina superior izquierda
        int x = cordenadaXcentro - ancho/2;
        int y = cordenadaYcentro - alto/2;
        //Definimos el rectangulo donde dibujaremos nuestro drawable con (izquierda,ariba,derecha y abajo)
        imagen.setBounds(x,y,x+ancho, y+alto);
        canvas.save(); //Guardamos la imagen
        //Rotamos el rectangulo respecto al ángulo y la cordenada (x,y) del centro como punto de anclaje
        canvas.rotate((float)angulo, cordenadaXcentro,cordenadaYcentro); //rotamos la imagen con un  angulo tomando las cordenas (x,y) como punto de anclaje
        //Dibujamos la imagen
        imagen.draw(canvas);
        //Restablecemos la modificaciones que hemos hecho
        canvas.restore();
        //Le indicamos a vista que va tener que redibujar el drawable en el rectángulo ya que lo hemos aplicado una rotacion
        view.invalidate(cordenadaXcentro-radioInval,cordenadaYcentro-radioInval,cordenadaYcentro+radioInval,cordenadaYcentro+radioInval);
        //Le indicamos a la vista que hemos desplazado el drawable por lo que lo debe de redibujar en donde se encontraba anteroirmente
        view.invalidate(cordenadaXanterior-radioInval,cordenadaYanterior-radioInval, cordenadaXanterior+radioInval,cordenadaYanterior+radioInval);
        //Como se desplazo el drawable guardamos la cordenadas del (x,y) del centro como anteriores
        cordenadaXanterior = cordenadaXcentro;
        cordenadaYanterior = cordenadaYcentro;
    }
    /** Incrementa las posición (x,y) del centro y el ángulo de nuestro drawable de acuerdo a un factor que recibimos,para posteriormete
     * verificar si el drawble se salio de la pantalla, indicando que su posición (x,Y) va a cambiar de acuedo por donde se salio
     * de la pantalla. Si salio de ariba su posición va a cambiar para que regrese en la parte de abajo, así sucesivamente*/
    public void incrementaPosicion(double factor){
        //Aumentamos la cordenda (x,y) del centro así como su ángulo
        cordenadaXcentro += incrementoX * factor;
        cordenadaYcentro += incrementoY * factor;
        angulo += velocidadRotacion * factor;
        //Si el drawabel sale por la izquierda va a regresar por la derecha
        if(cordenadaXcentro < 0) {
            cordenadaXcentro = view.getWidth();
            //Si el drawabel sale por la derecha va a regresar por la izquierda
        }else if(cordenadaXcentro > view.getWidth()){
            cordenadaXcentro = 0;
        }
        //Si el drawabel sale por arriba va a regresar por abajo
        if (cordenadaYcentro < 0){
            cordenadaYcentro = view.getHeight();
            //Si el drawabel sale por abajo va a regresar por ariba
        }else if (cordenadaYcentro > view.getHeight()){
            cordenadaYcentro = 0;
        }

    }
    /** Calulamos la distancia de un drawable con otro usando el teorema de Pitágoras, restando la posición de las x de cada
     * drawable así como restando las y de cada drawable*/
    public double distancia(Grafico grafico){
        return Math.hypot(cordenadaXcentro - grafico.cordenadaXcentro, cordenadaYcentro - grafico.cordenadaYcentro);
    }
    /** Función que nos regresa un true si hay una colisión, y un false si no hay colicion. Esto se calcula si la distancia entre
     * ambos drawables es menor a la suma de los radios de colision de ambos drawables */
    public boolean verificarColision(Grafico grafico){
        return distancia(grafico) < (radioColision + grafico.radioColision);
    }
     /*Geter and seter*/

    public Drawable getImagen() {
        return imagen;
    }

    public int getCordenadaXcentro() {
        return cordenadaXcentro;
    }

    public void setCordenadaXcentro(int cordenadaXcentro) {
        this.cordenadaXcentro = cordenadaXcentro;
    }

    public int getCordenadaYcentro() {
        return cordenadaYcentro;
    }

    public void setCordenadaYcentro(int cordenadaYcentro) {
        this.cordenadaYcentro = cordenadaYcentro;
    }

    public int getAncho() {
        return ancho;
    }

    public double getIncrementoX() {
        return incrementoX;
    }

    public void setIncrementoX(double incrementoX) {
        this.incrementoX = incrementoX;
    }

    public double getIncrementoY() {
        return incrementoY;
    }

    public void setIncrementoY(double incrementoY) {
        this.incrementoY = incrementoY;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public void setVelocidadRotacion(double velocidadRotacion) {
        this.velocidadRotacion = velocidadRotacion;
    }

}
