package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.proyectogrupo1musicstore.R;

public class ActivityArchivosPersonales extends AppCompatActivity {
    ImageView subirMultimedia,btnAtrasPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivo_personales);
        subirMultimedia = (ImageView) findViewById(R.id.subirMultimedias);
        btnAtrasPlay  = (ImageView) findViewById(R.id.btnAtrasss);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;

                if (view.getId() == R.id.btnAtrasss) {
                    actividad = ActivityPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        btnAtrasPlay.setOnClickListener(buttonClick);

    }

    public void subirMultimedia (View view){
        Intent pantallaPrincipal = new Intent(getApplicationContext(), Activity_SubirMusica.class);
        startActivity(pantallaPrincipal);

    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);

    }
}