package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Joystick {

    private int radioCirculoExterior;
    private int radioCirculoInterior;
    private int cordenadaXcirculoExterior;
    private int cordenadaYcirculoExterior;
    private int cordenadaXcirculoInterior;
    private int cordenadaYcirculoInterior;
    private Paint circuloExteriorPaint, circuloInteriorPaint;

    public Joystick(int cordenadaX, int cordenadaY, int radioCirculoExt, int radioCirculoInt){

        cordenadaXcirculoExterior = cordenadaX;
        cordenadaYcirculoExterior = cordenadaY;
        cordenadaXcirculoInterior = cordenadaX;
        cordenadaYcirculoInterior = cordenadaY;

        radioCirculoExterior = radioCirculoExt;
        radioCirculoInterior = radioCirculoInt;

        circuloExteriorPaint = new Paint();
        circuloExteriorPaint.setColor(Color.GRAY);
        circuloExteriorPaint.setStrokeWidth(3);
        circuloExteriorPaint.setStyle(Paint.Style.STROKE);

        circuloInteriorPaint = new Paint();
        circuloInteriorPaint.setColor(Color.LTGRAY);
        circuloInteriorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(cordenadaXcirculoExterior, cordenadaYcirculoExterior, radioCirculoExterior, circuloExteriorPaint);
        canvas.drawCircle(cordenadaXcirculoInterior, cordenadaYcirculoInterior, radioCirculoInterior, circuloInteriorPaint);


    }
}
