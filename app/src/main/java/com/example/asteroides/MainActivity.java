package com.example.asteroides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnJugar, btnSalir, btnConfigurar, btnAcercaDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJugar = (Button) findViewById(R.id.botonIniciar);
        btnAcercaDe = (Button) findViewById(R.id.botonAcercaDe);
        btnSalir = (Button) findViewById(R.id.botonSalir);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambiarVentana = new Intent(MainActivity.this, Juego.class);
                startActivity(cambiarVentana);
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mostrarAcercaDe = new Intent(MainActivity.this, AcercaDe.class);
                startActivity(mostrarAcercaDe);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
