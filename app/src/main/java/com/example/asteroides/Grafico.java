package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafico {
    private Drawable imagen; //Drawable que vamos a mostrar (nave, asteroide ó disparo)
    private int cordenadaXcentro,cordenadaYcentro; //Cordenadas (x,y) del centro de nuestro drawable
    private int alto,ancho; //Alto y ancho del drawable
    private double incrementoX, incrementoY;// Incremento de velocidad en eje de X y de Y
    private double angulo, velocidadRotacion; //Angulo y velocidad de rotación del drawable
    private int radioColicion; //Radio que nos indica cuando un objeto se cruza con otro y esto generará una colicion
    private int cordenadaXanterior,cordenadaYanterior; //Cordenanda (x,y) anterior del drawable
    private int radioInval; //Incrementamos el radio de la imagen, cuando esta fue rotada
    private View view;

    /** Inicializamos llamando a grafico con la vista donde se va a mostrar la imagen asi como el nombre de la drawable,
     *  seguido sacamos el ancho y alto del drawable, calculamos el radio de colision y por último el radio inval que
     *  representa un radio extendido ya que la imagen rotará por lo que abarcará más espacio
     * */
    public Grafico(View view, Drawable imagen){
        this.view = view;
        this.imagen = imagen;
        ancho = imagen.getIntrinsicWidth(); //obtenemos en ancho de la imagen
        alto = imagen.getIntrinsicHeight();//obtenemos el alto de la imagen
        radioColicion = (alto + ancho) / 2; //calculamos en radio de colicion
        radioInval = (int) Math.hypot(ancho / 2 , alto / 2);
    }

    /** Dibujamos el drawable en la posición (x,y) y gurdamos los cambios para posteriormente rotarla de acuerdo
     * al ángulo que le asignamos. Una vez dibujada la imagen (drawable), le decimos al canvas que hay que redibujar solo el
     * rectangulo que hemos modificado y le pasamos las medidas del rectangulo. El drawable se mueve y por eso guardamos las
     * cordenas (x,y) anteriores*/
    public void dibujarGrafico(Canvas canvas){
        int x = cordenadaXcentro - ancho/2;
        int y = cordenadaYcentro - alto/2;
        imagen.setBounds(x,y,x+ancho, y+alto);// indicamos el rectangulo en donde se va a dibujar la imagen en izquierda,ariba,derecha,abajo
        canvas.save(); //guardamos los cambios de la imagen
        canvas.rotate((float)angulo, cordenadaXcentro,cordenadaYcentro); //rotamos la imagen con un  angulo tomando las cordenas (x,y) como punto de anclaje
        imagen.draw(canvas); //dibujamos nuestra imagen con una posicion (x,y) y un angulo de rotación
        canvas.restore();
        view.invalidate(cordenadaXcentro-radioInval,cordenadaYcentro-radioInval,cordenadaYcentro+radioInval,cordenadaYcentro+radioInval);
        view.invalidate(cordenadaXanterior-radioInval,cordenadaYanterior-radioInval, cordenadaXanterior+radioInval,cordenadaYanterior+radioInval);
        cordenadaXanterior = cordenadaXcentro;
        cordenadaYanterior = cordenadaYcentro;
    }
    /** Incrementa las posición (x,y) y el ángulo de nuestro drawable de acuerdo a un factor para posteriormete
     * verificar si la cordenada de (x,y) es menor que cero lo que indicará que nuestro objeto se salio de la pantalla
     * por lo que vamos a tomar el ancho y alto de nuestra vista y si la coordenada (x,y) es mayor que la vista la vamos a
     * colocarla  en (0,0) */
    public void incrementaPosicion(double factor){
        cordenadaXcentro += incrementoX * factor;
        cordenadaYcentro += incrementoY * factor;
        angulo += velocidadRotacion * factor;

        if(cordenadaXcentro < 0) {
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
    /** */
    public double distancia(Grafico grafico){
        return Math.hypot(cordenadaXcentro - grafico.cordenadaXcentro, cordenadaYcentro - grafico.cordenadaYcentro);
    }
    /** */
    public boolean verificarColision(Grafico grafico){
        return distancia(grafico) < (radioColicion + grafico.radioColicion);
    }
     /*Geter and seter*/

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

    public double getVelocidadRotacion() {
        return velocidadRotacion;
    }

    public void setVelocidadRotacion(double velocidadRotacion) {
        this.velocidadRotacion = velocidadRotacion;
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
