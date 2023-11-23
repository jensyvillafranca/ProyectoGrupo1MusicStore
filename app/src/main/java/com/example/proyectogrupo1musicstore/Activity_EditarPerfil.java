package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_EditarPerfil extends AppCompatActivity {

    ImageView btnAtras;
    TextView txtviewCambiarContrasenia;
    ImageView imgContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        btnAtras = (ImageView) findViewById(R.id.btn_EditarPerfilAtras);
        imgContrasenia=findViewById(R.id.imgContrasenia);
        txtviewCambiarContrasenia=findViewById(R.id.txtviewCambiarContrasenia);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_EditarPerfil.this, Activity_PerfilPersonal.class);
                startActivity(intent);
            }
        });

        imgContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_CambiarContrasena.class);
                startActivity(intent);
            }
        });

        txtviewCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_CambiarContrasena.class);
                startActivity(intent);
            }
        });

    }
}
