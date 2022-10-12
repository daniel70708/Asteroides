package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AndroidException;

import androidx.core.content.ContextCompat;

class Joystick {

    private Context context;
    private int radioCirculoExterior, radioCirculoInterior;
    private int cordenadaXcirculoExterior, cordenadaYcirculoExterior;
    private int cordenadaXcirculoInterior, cordenadaYcirculoInterior;
    private Paint circuloExteriorPaint, circuloInteriorPaint;
    private double distanciaJoystick;
    private boolean presionado;
    private double actuadorX,actuadorY;

    public Joystick(int cordenadaX, int cordenadaY, int radioCirculoExt, int radioCirculoInt){

        cordenadaXcirculoExterior = cordenadaX;
        cordenadaYcirculoExterior = cordenadaY;
        cordenadaXcirculoInterior = cordenadaX;
        cordenadaYcirculoInterior = cordenadaY;

        radioCirculoExterior = radioCirculoExt;
        radioCirculoInterior = radioCirculoInt;

        int blancoPocaTrasparencia = Color.argb(200,255,255,255);
        circuloExteriorPaint = new Paint();
        circuloExteriorPaint.setColor(blancoPocaTrasparencia);
        circuloExteriorPaint.setStrokeWidth(2);
        circuloExteriorPaint.setStyle(Paint.Style.STROKE);

        int blancoTransparente = Color.argb(100, 255,255,255);
        circuloInteriorPaint = new Paint();
        circuloInteriorPaint.setColor(blancoTransparente);
        circuloInteriorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(cordenadaXcirculoExterior, cordenadaYcirculoExterior, radioCirculoExterior, circuloExteriorPaint);
        canvas.drawCircle(cordenadaXcirculoInterior, cordenadaYcirculoInterior, radioCirculoInterior, circuloInteriorPaint);
    }

    public void update(){
        actualizarPosicionCirculoInterior();
    }
    public void actualizarPosicionCirculoInterior(){
        cordenadaXcirculoInterior = (int) (cordenadaXcirculoExterior + (actuadorX * radioCirculoExterior));
        cordenadaYcirculoInterior = (int) (cordenadaYcirculoExterior + (actuadorY * radioCirculoExterior));
    }

    public boolean esPresionado(double posicionXusuario, double posicionYusuario) {
        double catetoA = (cordenadaXcirculoExterior - posicionXusuario);
        double catetoB = (cordenadaYcirculoExterior - posicionYusuario);
        distanciaJoystick = Math.hypot(catetoA, catetoB);

        return distanciaJoystick < radioCirculoExterior;
    }

    public void setPresionado(boolean bandera) {
        presionado = bandera;
    }

    public boolean getPresionado() {
        return presionado;
    }

    public void setActuador(double posicionXusuario, double posicionYusuario) {
        double deltaX = (posicionXusuario - cordenadaXcirculoExterior);
        double deltaY = (posicionYusuario - cordenadaYcirculoExterior);
        double distanciaDelta = Math.hypot(deltaX,deltaY);
        if (distanciaDelta < radioCirculoExterior){
            actuadorX = deltaX / radioCirculoExterior;
            actuadorY = deltaY / radioCirculoExterior;
        }else {
            actuadorX = deltaX / distanciaDelta;
            actuadorY = deltaY / distanciaDelta;
        }
    }

    public void reiniciarActuador() {
        actuadorX = 0.0;
        actuadorY = 0.0;
    }

    public double getActuadorX(){
        return actuadorX;
    }
    public double getActuadorY(){
        return actuadorY;
    }
}
