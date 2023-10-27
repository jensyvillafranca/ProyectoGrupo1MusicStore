package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button prueba, btnPantallaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*prueba = (Button) findViewById(R.id.prueba);
        btnPantallaPrincipal = (Button) findViewById(R.id.btnPruebaPantallaPrincipal);

        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prueba = new Intent(getApplicationContext(),activity_registrarse.class);
                startActivity(prueba);
            }
        });*/

        btnPantallaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaPrincipal = new Intent(getApplicationContext(),ActivityPantallaPrincipal.class);
                startActivity(pantallaPrincipal);
            }
        });
    }
}