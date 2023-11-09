package com.example.proyectogrupo1musicstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_EditarPerfil extends AppCompatActivity {

    LinearLayout btnAtras;
    ImageView btnCambiarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        btnAtras = findViewById(R.id.btn_EditarPerfilAtras);
        btnCambiarContrasena=findViewById(R.id.imgContrasena);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_EditarPerfil.this, Activity_PerfilPersonal.class);
                startActivity(intent);
            }
        });

        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_EditarPerfil.this, Activity_CambiarContrasena.class);
                startActivity(intent);
            }
        });

    }
}
