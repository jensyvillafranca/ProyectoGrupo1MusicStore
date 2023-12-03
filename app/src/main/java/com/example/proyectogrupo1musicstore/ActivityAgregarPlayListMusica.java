package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosIntegrantes;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosMusica;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosVideo;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;

public class ActivityAgregarPlayListMusica extends AppCompatActivity {
    TextView agregarCanciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_play_list_musica);
        agregarCanciones = (TextView) findViewById(R.id.textviewSiguienteAgregar);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.txtViewNavGrupos) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textviewSiguienteAgregar) {
                    actividad = Activity_SubirMusica.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        agregarCanciones.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}