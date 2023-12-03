package com.example.proyectogrupo1musicstore.Utilidades.Navegacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGruposBuscar;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosIntegrantes;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosMusica;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosVideo;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.R;

public class NavigationGruposInfoClickListener implements View.OnClickListener {

    private final Context context;
    private Activity activity;
    private int tipo;
    private int idGrupo;
    //Elementos de menu de navegacion
    private ImageButton botonAtras;
    private TextView textviewAtras, verTodosIntegrantes, verTodosMusica, verTodosVideo;




    public NavigationGruposInfoClickListener(Activity activity, Context context, int idGrupo) {

        this.context = context;
        this.activity = activity;
        this.idGrupo = idGrupo;

        this.botonAtras = activity.findViewById(R.id.btn_GrupoInfoAtras);
        this.textviewAtras = activity.findViewById(R.id.textview_GrupoInfoBotAtras);
        this.verTodosIntegrantes = activity.findViewById(R.id.textviewVerTodoIntegrantes);
        this.verTodosMusica = activity.findViewById(R.id.textviewVerTodoMusica);
        this.verTodosVideo = activity.findViewById(R.id.textviewVerTodoVideo);


        // Set click listeners
        botonAtras.setOnClickListener(this);
        textviewAtras.setOnClickListener(this);
        verTodosIntegrantes.setOnClickListener(this);
        verTodosMusica.setOnClickListener(this);
        verTodosVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<?> actividad = null;

        if (view.getId()==R.id.btn_GrupoInfoAtras) {
            activity.finish();
            tipo = 1;
        }
        if (view.getId()==R.id.textview_GrupoInfoBotAtras){
            activity.finish();
            tipo = 1;
        }
        if (view.getId() == R.id.textviewVerTodoIntegrantes) {
            actividad = ActivityVerTodosIntegrantes.class;
            tipo = 2;
        }
        if (view.getId() == R.id.textviewVerTodoMusica) {
            actividad = ActivityVerTodosMusica.class;
            tipo = 2;
        }
        if (view.getId() == R.id.textviewVerTodoVideo) {
            actividad = ActivityVerTodosVideo.class;
            tipo = 2;
        }
        if (actividad != null) {
            if(tipo == 1){
                moveActivity(actividad);
            }else{
                moveActivity2(actividad, idGrupo);
            }
        }
    }

    private void moveActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private void moveActivity2(Class<?> actividad, int idgrupo) {
        Intent intent = new Intent(context, actividad);
        intent.putExtra("idgrupo", idgrupo);
        context.startActivity(intent);
    }
}


