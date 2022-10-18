package com.example.asteroides;

import android.view.View;

public class PosicionamientoJoystick {

    private int altoPantalla, anchoPantalla;
    private int radioExterior, radioInterior;
    private int posicionXjoystick, posicionYjoystick;
    private View view;

    /** Clase que determina la posicion (x,y) y el radio de ambos circulos que va a tener el joystick
     * de acuerdo al tamaño de pantalla del dispositivo */
    public PosicionamientoJoystick(View view) {
        this.view = view;
        //Obtenemos el alto y el ancho de la pantalla en pixeles
        altoPantalla = view.getResources().getDisplayMetrics().heightPixels;
        anchoPantalla = view.getResources().getDisplayMetrics().widthPixels;

        radioExterior = anchoPantalla / 12; //Tomamos la doceava parte del ancho de la pantalla para el radio exterior
        radioInterior = (radioExterior / 2) + 1; //Tomamos la mitad del radio exterior y le sumamos uno

        posicionXjoystick = (anchoPantalla / 10) * 2; //Dividimos el ancho de la pantalla en 10 y tomamos dos partes
        //Dividimos el alto de la pantalla en 10 y tomamos 3 partes, para restarle al alto de la pantalla tres partes que calculamos
        posicionYjoystick = altoPantalla - ( (altoPantalla / 10) * 3);
    }

    /*Geter*/
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

    public int getAltoPantalla() {
        return altoPantalla;
    }

    public int getAnchoPantalla() {
        return anchoPantalla;
    }
}
