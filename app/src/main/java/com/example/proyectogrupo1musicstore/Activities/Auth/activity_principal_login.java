package com.example.proyectogrupo1musicstore.Activities.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectogrupo1musicstore.R;

public class activity_principal_login extends AppCompatActivity {

    /*Declaración de las variables*/
    Button registrate, acceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*Amarrando las variables al elemento de la interfaz*/
        registrate =(Button)findViewById(R.id.btnRegistrarse);
        acceder =(Button)findViewById(R.id.btnIngresar);


        /*Evento del boton de ir a la ventana de login*/
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), activity_login.class);
                startActivity(login);
            }
        });

        /*Evento del boton de ir a la ventana de registrarse*/
        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrate = new Intent(getApplicationContext(), activity_registrarse.class);
                startActivity(registrate);
            }
        });
    }
}
