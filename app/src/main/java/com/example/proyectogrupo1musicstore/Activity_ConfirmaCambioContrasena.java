package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Activity_ConfirmaCambioContrasena extends AppCompatActivity {

    LinearLayout btnAtras;
    Button btnVerificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_cambio_contrasena);

        btnAtras = findViewById(R.id.btn_ConfirmarCambiarContrasenaAtras);
        btnVerificar = findViewById(R.id.btn_Verificar);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegresarCambioContrasena = new Intent(Activity_ConfirmaCambioContrasena.this, Activity_CambiarContrasena.class);
                startActivity(RegresarCambioContrasena);
            }
        });

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VerificarEditarPerfil = new Intent(Activity_ConfirmaCambioContrasena.this, Activity_EditarPerfil.class);
                startActivity(VerificarEditarPerfil);
            }
        });
    }
}