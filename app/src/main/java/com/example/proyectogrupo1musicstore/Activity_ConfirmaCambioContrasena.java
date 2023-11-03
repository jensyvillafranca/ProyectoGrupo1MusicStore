package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Activity_ConfirmaCambioContrasena extends AppCompatActivity {

    LinearLayout btnAtras = (LinearLayout) findViewById(R.id.btn_ConfirmarCambiarContrasenaAtras);
    Button btnVerificar = (Button) findViewById(R.id.btn_Verificar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_cambio_contrasena);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegresarCambioContrasena = new Intent(getApplicationContext(), Activity_CambiarContrasena.class);
                startActivity(RegresarCambioContrasena);
            }
        });

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VerificarEditarPerfil = new Intent(getApplicationContext(), Activity_EditarPerfil.class);
                startActivity(VerificarEditarPerfil);
            }
        });
    }
}