package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafico {
    private Drawable imagen; //Imagen que vamos a mostrar en el juego
    private int cordenadaXcentro,cordenadaYcentro; //Cordenadas (x,y) del centro de nuestra imagen
    private int alto,ancho; //Alto y ancho de la imagen
    private double cordenadaXincremento,cordenadaYincremento;//Posicion (x,y) del incremento de velocidad
    private double angulo, rotacion; //Angulo y rotación de nuestra imagen en el juego
    private int radioColicion; //Radio que nos indica cuando un objeto se cruza con otro y esto generará una colicion
    private int cordenadaXanterior,cordenadaYanterior; //Cordenanda (x,y) anterior de nuestro gráfico
    private int radioInval;
    private View view;

    public Grafico(View view, Drawable imagen){
        this.view = view;
        this.imagen = imagen;
        ancho = imagen.getIntrinsicWidth(); //obtenemos en ancho de la imagen
        alto = imagen.getIntrinsicHeight();//Obtenemos el alto de la imagen
        radioColicion = (alto+ancho)/2; //Calculamos en radio de colicion
        radioInval = (int) Math.hypot(ancho/2,alto/2);
    }

    public void dibujarGrafico(Canvas canvas){
        int x = cordenadaXcentro - ancho/2;
        int y = cordenadaYcentro - alto/2;
        imagen.setBounds(x,y,x+ancho, y+alto);//se va a dibujar la imagen en izquierda,ariba,derecha,abajo
        canvas.save(); //Guardamos los cambios de la imagen
        canvas.rotate((float)angulo, cordenadaXcentro,cordenadaYcentro); //Rotamos la imagen con un  angulo tomando las cordenas (x,y) como punto de anclaje
        imagen.draw(canvas); //Dibujamos nuestra imagen con una posicion (x,y) y un angulo de rotación
        canvas.restore();
        view.invalidate(cordenadaXcentro-radioInval,cordenadaYcentro-radioInval,cordenadaYcentro+radioInval,cordenadaYcentro+radioInval);
        view.invalidate(cordenadaXanterior-radioInval,cordenadaYanterior-radioInval, cordenadaXanterior+radioInval,cordenadaYanterior+radioInval);
        cordenadaXanterior = cordenadaXcentro;
        cordenadaYanterior = cordenadaYcentro;
    }

    public void incrementaPosicion(double factor){
        cordenadaXcentro += cordenadaXincremento * factor;
        cordenadaYcentro += cordenadaYincremento * factor;
        angulo += rotacion * factor;
        if(cordenadaYcentro < 0) {
            cordenadaXcentro = view.getWidth();
        }else if(cordenadaXcentro > view.getWidth()){
            cordenadaXcentro = 0;
        }
        if (cordenadaYcentro < 0){
            cordenadaYcentro = view.getHeight();
        }else if (cordenadaYcentro > view.getHeight()){
            cordenadaYcentro = 0;
        }
    }

    public double distancia(Grafico grafico){
        return Math.hypot(cordenadaXcentro - grafico.cordenadaXcentro, cordenadaYcentro - grafico.cordenadaYcentro);
    }

    public boolean verificarColision(Grafico grafico){
        return distancia(grafico) < (radioColicion + grafico.radioColicion);
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
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

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public double getCordenadaXincremento() {
        return cordenadaXincremento;
    }

    public void setCordenadaXincremento(double cordenadaXincremento) {
        this.cordenadaXincremento = cordenadaXincremento;
    }

    public double getCordenadaYincremento() {
        return cordenadaYincremento;
    }

    public void setCordenadaYincremento(double cordenadaYincremento) {
        this.cordenadaYincremento = cordenadaYincremento;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public double getRotacion() {
        return rotacion;
    }

    public void setRotacion(double rotacion) {
        this.rotacion = rotacion;
    }

    public int getRadioColicion() {
        return radioColicion;
    }

    public void setRadioColicion(int radioColicion) {
        this.radioColicion = radioColicion;
    }

    public int getCordenadaXanterior() {
        return cordenadaXanterior;
    }

    public void setCordenadaXanterior(int cordenadaXanterior) {
        this.cordenadaXanterior = cordenadaXanterior;
    }

    public int getCordenadaYanterior() {
        return cordenadaYanterior;
    }

    public void setCordenadaYanterior(int cordenadaYanterior) {
        this.cordenadaYanterior = cordenadaYanterior;
    }

    public int getRadioInval() {
        return radioInval;
    }

    public void setRadioInval(int radioInval) {
        this.radioInval = radioInval;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
