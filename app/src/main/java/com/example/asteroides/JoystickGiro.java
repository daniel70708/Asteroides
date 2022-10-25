package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class JoystickGiro {
    private int radioCirculoExteriorGiro,cordenadaXcirculoExtGiro, cordenadaYcirculoExtGiro;
    private int radioCirculoInteriorGiro,cordenadaXcirculoIntGiro,cordenadaYcirculoIntGiro;
    private Paint circuloExteriorPaint, circuloInteriorPaint;
    private double distanciaJoystick;
    private boolean presionado;
    private double actuadorX,actuadorY;

    /**Constructor de la clase con la que recibimos como parámetros la cordenada (x,y) de posición que tendŕa el joysctick,
     * tanto para el círculo exterior como el interior, además de el radio de cada círculo*/
    public JoystickGiro(int cordenadaX, int cordenadaY, int radioCiculoExterior, int radioCirculoInterior) {
            //Asignamos la cordenada (x,y) a ambos circulos del joystick
            cordenadaXcirculoExtGiro = cordenadaX;
            cordenadaYcirculoExtGiro = cordenadaY;
            cordenadaXcirculoIntGiro = cordenadaX;
            cordenadaYcirculoIntGiro = cordenadaY;

            //Asignamos el radio a cada circulo
            radioCirculoExteriorGiro = radioCiculoExterior;
            radioCirculoInteriorGiro = radioCirculoInterior;

        //Definimos las características que van a tener el circulo exterior e interior
        int blancoPocaTrasparencia = Color.argb(200,255,255,255); //Creamos un color blanco con poca trasparencia para el circulo exterior
        circuloExteriorPaint = new Paint();
        circuloExteriorPaint.setColor(blancoPocaTrasparencia);
        circuloExteriorPaint.setStrokeWidth(2); //El grosor de la linea va ser un poco más pronunciado que lo normal
        circuloExteriorPaint.setStyle(Paint.Style.STROKE); //Solo dibujamos el contorno del circulo sin relleno

        //Creamos un color blanco con un 40% de tranparencia, para que podemos observar asteroides si es que pasan por el joystick
        int blancoTransparente = Color.argb(100, 255,255,255);
        circuloInteriorPaint = new Paint();
        circuloInteriorPaint.setColor(blancoTransparente);
        circuloInteriorPaint.setStyle(Paint.Style.FILL_AND_STROKE); //Dibujamos el contorno y el relleno del circulo
    }

    /**Método con el que vamos a dibujar ambos circulos que componen el joystick*/
    public void draw(Canvas canvas) {
        //Para dibujar el circulo le pasamos como parámetros posición (x,y), el radio del circulo y las caráteristicas del Paint
        canvas.drawCircle(cordenadaXcirculoExtGiro, cordenadaYcirculoExtGiro, radioCirculoExteriorGiro, circuloExteriorPaint);
        canvas.drawCircle(cordenadaXcirculoIntGiro, cordenadaYcirculoIntGiro, radioCirculoInteriorGiro, circuloInteriorPaint);
    }

    /** Método que llamamos de manera recurrente para actualizar la posición (x,y) del círculo interior del joystick*/
    public void update(){
        actualizarPosicionCirculoInterior();
    }

    /** Método que calcula la nueva posición (x,y) del circulo interior con respecto al actuadorX y al actuadorY,
     * que indican hacia donde el usario esta realizando el movimiento*/
    public void actualizarPosicionCirculoInterior(){
        //A la cordenada inicial le sumamos la multiplicación del actuador por el radio del círculo exterior
        cordenadaXcirculoIntGiro = (int) (cordenadaXcirculoExtGiro + (actuadorX * radioCirculoExteriorGiro));
        cordenadaYcirculoIntGiro = (int) (cordenadaYcirculoExtGiro + (actuadorY * radioCirculoExteriorGiro));
    }

    /** Método que nos regresa un true si el usuario esta tocando la pantalla dentro del circulo exterior de nuestro
     * joystick y un false si el toque a la pantalla es fuera del joystick. Este cálculo se realiza restando la distancia
     * entre el toque del usuario y la posición del joystick (usando el teorema de Pitágoras)*/
    public boolean esPresionado(double posicionXusuario, double posicionYusuario) {
        double catetoA = (cordenadaXcirculoExtGiro - posicionXusuario); //Restamos la cordenada X del joystick al toque X de la pantalla
        double catetoB = (cordenadaYcirculoExtGiro - posicionYusuario); //Restamos la cordenada Y del joystick al toque Y de la pantalla
        distanciaJoystick = Math.hypot(catetoA, catetoB); //Calculamos la distancia con el teorema de Pitágoras

        //Si la distancia es menor que el radio del circulo del joystick, entonces sabemos que el toque es dentro del joystick
        return distanciaJoystick < radioCirculoExteriorGiro;
    }

    /** Cambiamos el valor de boleano presionado indicando que el usuario esta sobre el joystick o no*/
    public void setPresionado(boolean presionado) {
        this.presionado = presionado;
    }

    /**Obtenemos el valor de presionado*/
    public boolean getPresionado() {
        return presionado;
    }

    /**Función que calcula actuador x y actuador y, recibimos la posición (x,y) del toque del usuario y calculamos la distancia (usnado
     * el teorema de Pitágoras) entre la posición que recibimos y la posición del joystick. Si la distancia resultante es mayor al radio
     * del joystick (círculo exterior) significa que el actuador indicará que el giro de la nave seŕa a máxima velocidad y si
     * por el contrario es menor al radio del círculo exterior significa que el giro será moderado*/
    public void setActuador(double posicionXusuario, double posicionYusuario) {
        //Restamos la cordenada X del toque del usuario al cordenada X del círculo exterior
        double deltaX = (posicionXusuario - cordenadaXcirculoExtGiro);
        //Restamos la cordenada Y del toque del usuario al cordenada Y del círculo exterior
        double deltaY = (posicionYusuario - cordenadaYcirculoExtGiro);
        //Calculamos la distancia entre ambas posiciones
        double distanciaDelta = Math.hypot(deltaX,deltaY);
        //Si la distanciaDelta es menor al radio del circulo exterior significa que el giro es moderado
        if (distanciaDelta < radioCirculoExteriorGiro){
            //El valor del actuador x será deltaX entre radio del círculo exterior
            actuadorX = deltaX / radioCirculoExteriorGiro;
            //El valor del actuador y será deltaY entre radio del círculo exterior
            actuadorY = deltaY / radioCirculoExteriorGiro;
        }else { //Significa que el moviento del joystick esta fuera del circulo del joystick por lo que el giro es a máxima velocidad
            //El valor del actuador x será deltaX entre la distancia de ambas x
            actuadorX = deltaX / distanciaDelta;
            //El valor del actuador y será deltaY entre la distancia de ambas y
            actuadorY = deltaY / distanciaDelta;
        }
    }

    /** Reiniciamos los valores de los actuadores, cuando el usuario deje de pulsar y mover el joystick*/
    public void reiniciarActuador() {
        actuadorX = 0.0;
        actuadorY = 0.0;
    }

    /**Obtenemos el actuador de X como de Y*/
    public double getActuadorX() {
        return actuadorX;
    }

    public double getActuadorY() {
        return actuadorY;
    }
}
