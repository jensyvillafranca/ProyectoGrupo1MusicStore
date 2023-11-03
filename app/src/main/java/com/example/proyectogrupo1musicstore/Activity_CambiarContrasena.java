package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Activity_CambiarContrasena extends AppCompatActivity {
    LinearLayout btnCambiarAtras = (LinearLayout) findViewById(R.id.btn_CambiarContrasenaAtras);
    Button btnEnviarCodigo = (Button) findViewById(R.id.btnEnviar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        btnCambiarAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegresarCambiarContrasena= new Intent(getApplicationContext(), Activity_EditarPerfil.class);
                startActivity(RegresarCambiarContrasena);
            }
        });


        btnEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnviarCodigo = new Intent(getApplicationContext(), Activity_ConfirmaCambioContrasena.class);
                startActivity(EnviarCodigo);
            }
        });



    }
}