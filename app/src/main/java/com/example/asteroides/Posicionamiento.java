package com.example.asteroides;

import android.view.View;

public class Posicionamiento {

    private int altoPantalla, anchoPantalla;
    private int radioExterior, radioInterior;
    private int posicionXjoystick, posicionYjoystick;
    private View view;

    public Posicionamiento(View view) {
        this.view = view;

        altoPantalla = view.getResources().getDisplayMetrics().heightPixels;
        anchoPantalla = view.getResources().getDisplayMetrics().widthPixels;

        radioExterior = anchoPantalla / 12;
        radioInterior = (radioExterior / 2) + 1;

        posicionXjoystick = (anchoPantalla / 10) * 2;
        int x = (altoPantalla / 10) * 3;
        posicionYjoystick = altoPantalla - x;

    }

    public int getRadioExterior() {
        return radioExterior;
    }

    public int getRadioInterior() {
        return radioInterior;
    }

    public int getPosicionXjoystick() {
        return posicionXjoystick;
    }

    public int getPosicionYjoystick() {
        return posicionYjoystick;
    }

}
