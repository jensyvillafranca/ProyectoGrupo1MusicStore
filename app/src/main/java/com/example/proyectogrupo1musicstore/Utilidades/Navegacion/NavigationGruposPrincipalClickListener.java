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
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.R;

public class NavigationGruposPrincipalClickListener implements View.OnClickListener {

    private final Context context;
    //Elementos de menu de navegacion
    private ImageButton botonAtras;
    private TextView textviewAtras, txtGruposBuscar, txtNuevoGrupo, txtActualizar;
    private ImageView imageviewGruposBuscar, imageviewNuevoGrupo, imageviewActualizar;
    private CardView buscar, nuevoGrupo, actualizar;
    private Activity activity;



    public NavigationGruposPrincipalClickListener(Activity activity, Context context) {

        this.context = context;
        this.activity = activity;

        this.botonAtras = activity.findViewById(R.id.btn_GruposPrincipalAtras);
        this.textviewAtras = activity.findViewById(R.id.textview_GrupoPrincipalbotAtras);
        this.txtGruposBuscar = activity.findViewById(R.id.txtGruposPrincipalBuscarGrupo);
        this.txtNuevoGrupo = activity.findViewById(R.id.txtGruposPrincipalNuevoGrupo);
        this.txtActualizar = activity.findViewById(R.id.txtGruposPrincipalActualizar);
        this.imageviewGruposBuscar = activity.findViewById(R.id.imageviewGruposPrincipalBuscar);
        this.imageviewNuevoGrupo = activity.findViewById(R.id.imageviewGruposPrincipalNuevoGrupo);
        this.imageviewActualizar = activity.findViewById(R.id.imageviewGruposPrincipalActualizar);
        this.buscar = activity.findViewById(R.id.cardViewGruposPrincipalBuscar);
        this.nuevoGrupo = activity.findViewById(R.id.cardViewGruposPrincipalNuevo);
        this.actualizar = activity.findViewById(R.id.cardViewGruposPrincipalActualizar);


        // Set click listeners
        botonAtras.setOnClickListener(this);
        textviewAtras.setOnClickListener(this);
        txtGruposBuscar.setOnClickListener(this);
        txtNuevoGrupo.setOnClickListener(this);
        txtActualizar.setOnClickListener(this);
        imageviewGruposBuscar.setOnClickListener(this);
        imageviewNuevoGrupo.setOnClickListener(this);
        imageviewActualizar.setOnClickListener(this);
        buscar.setOnClickListener(this);
        nuevoGrupo.setOnClickListener(this);
        actualizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<?> actividad = null;

        if (view.getId()==R.id.btn_GruposPrincipalAtras) {
            activity.finish();
        }
        if (view.getId()==R.id.textview_GrupoPrincipalbotAtras){
            activity.finish();
        }
        if (view.getId() == R.id.txtGruposPrincipalBuscarGrupo){
            actividad = ActivityGruposBuscar.class;
        }
        if (view.getId() == R.id.imageviewGruposPrincipalBuscar){
            actividad = ActivityGruposBuscar.class;
        }
        if (view.getId() == R.id.cardViewGruposPrincipalBuscar){
            actividad = ActivityGruposBuscar.class;
        }
        if (view.getId() == R.id.txtGruposPrincipalNuevoGrupo){
            actividad = ActivityNuevoGrupoIntegrantes.class;
        }
        if (view.getId() == R.id.imageviewGruposPrincipalNuevoGrupo){
            actividad = ActivityNuevoGrupoIntegrantes.class;
        }
        if (view.getId() == R.id.cardViewGruposPrincipalNuevo){
            actividad = ActivityNuevoGrupoIntegrantes.class;
        }
        if (view.getId() == R.id.txtGruposPrincipalActualizar){
            activity.recreate();
        }
        if (view.getId() == R.id.imageviewGruposPrincipalActualizar){
            activity.recreate();
        }
        if (view.getId() == R.id.cardViewGruposPrincipalActualizar){
            activity.recreate();
        }
        if (view.getId() == R.id.txtViewNavGrupos) {
            actividad = ActivityGrupoPrincipal.class;
        }
        if (view.getId() == R.id.txtviewNavInicio) {
            actividad = ActivityPantallaPrincipal.class;
        }
        if (view.getId() == R.id.iconNavGrupos){
            actividad = ActivityGrupoPrincipal.class;
        }
        if (view.getId() == R.id.iconNavInicio){
            actividad = ActivityPantallaPrincipal.class;
        }
        if (actividad != null) {
            moveActivity(actividad);
        }
    }

    private void moveActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }
}


