package com.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Vector;

public class VistaJuego extends View {
    //Asteroides
    private Vector<Grafico> asteroides;
    private int numeroAsteroides = 5;
    private int numeroFragmentos = 3;

    public VistaJuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide,drawableMisil;
        drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i<numeroAsteroides; i++){
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setCordenadaYincremento(Math.random() * 4 -2);
            asteroide.setCordenadaXincremento(Math.random() * 4 - 2);
            asteroide.setAngulo((int) Math.random() * 360);
            asteroide.setRotacion((int) Math.random() * 8 - 4);
            asteroides.add(asteroide);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior) {
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
        for (Grafico asteroide:asteroides){
            asteroide.setCordenadaXcentro((int) Math.random() * ancho);
            asteroide.setCordenadaYcentro((int) Math.random() * alto);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide:asteroides){
            asteroide.dibujarGrafico(canvas);
        }
    }
}
