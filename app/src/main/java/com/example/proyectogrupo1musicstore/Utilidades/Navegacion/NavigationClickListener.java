package com.example.proyectogrupo1musicstore.Utilidades.Navegacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Activities.AcercaDe.ActivityAcercaDe;
import com.example.proyectogrupo1musicstore.Activities.Buscar.ActivityBuscarPrincipal;
import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Activities.Multimedia.ActivityPlayList;
import com.example.proyectogrupo1musicstore.Activities.Perfil.Activity_EditarPerfil;
import com.example.proyectogrupo1musicstore.Activities.Perfil.Activity_PerfilPersonal;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.AppPreferences.AppPreferences;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_login;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_principal_login;

public class NavigationClickListener implements View.OnClickListener {

    private final Context context;
    //Elementos de menu de navegacion
    private TextView Inicio, Grupos, Perfil, Buscar, Multimedia, Ajustes, AcercaDe, CerrarSesion;
    private ImageView iconInicio, iconGrupos, iconPerfil, iconBuscar, iconMultimedia, iconAjustes, iconAcerca, iconCerrarSesion;
    private LinearLayout linearInicio, linearGrupos, linearPerfil, linearBuscar, linearMultimedia, linearAjustes, linearAcerca, linearCerrar;
    private token acceso;
    private Activity activity;



    public NavigationClickListener(Activity activity, Context context) {

        this.acceso = new token(context);
        this.context = context;
        this.Inicio = activity.findViewById(R.id.txtviewNavInicio);
        this.Grupos = activity.findViewById(R.id.txtViewNavGrupos);
        this.Perfil = activity.findViewById(R.id.txtviewNavUsuarios);
        this.Buscar = activity.findViewById(R.id.txtviewNavBuscar);
        this.Multimedia = activity.findViewById(R.id.txtviewNavMultimedia);
        this.Ajustes = activity.findViewById(R.id.txtviewNavAjustes);
        this.AcercaDe = activity.findViewById(R.id.txtviewNavAcerca);
        this.CerrarSesion = activity.findViewById(R.id.txtviewCerrarSesion);
        this.iconInicio = activity.findViewById(R.id.iconNavInicio);
        this.iconGrupos = activity.findViewById(R.id.iconNavGrupos);
        this.iconPerfil = activity.findViewById(R.id.iconNavUsuario);
        this.iconBuscar = activity.findViewById(R.id.iconNavBuscar);
        this.iconMultimedia = activity.findViewById(R.id.iconNavMultimedia);
        this.iconAjustes = activity.findViewById(R.id.iconNavAjustes);
        this.iconAcerca = activity.findViewById(R.id.iconNavAcerca);
        this.iconCerrarSesion = activity.findViewById(R.id.iconCerrarSesion);
        this.linearInicio = activity.findViewById(R.id.linearLayoutInicio);
        this.linearGrupos = activity.findViewById(R.id.linearLayoutGrupos);
        this.linearPerfil = activity.findViewById(R.id.linearLayoutPerfil);
        this.linearBuscar = activity.findViewById(R.id.linearLayoutBuscar);
        this.linearMultimedia = activity.findViewById(R.id.linearLayoutMultimedia);
        this.linearAjustes = activity.findViewById(R.id.linearLayoutAjustes);
        this.linearAcerca = activity.findViewById(R.id.linearLayoutAcerca);
        this.linearCerrar = activity.findViewById(R.id.linearLayoutCerrar);
        this.activity = activity;

        // Set click listeners
        linearInicio.setOnClickListener(this);
        linearGrupos.setOnClickListener(this);
        linearPerfil.setOnClickListener(this);
        linearBuscar.setOnClickListener(this);
        linearMultimedia.setOnClickListener(this);
        linearAjustes.setOnClickListener(this);
        linearAcerca.setOnClickListener(this);
        linearCerrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Class<?> actividad = null;

        // Elementos de menu navegacion - textview
        if (view == linearInicio) {
            actividad = ActivityPantallaPrincipal.class;
        } else if (view == linearGrupos) {
            actividad = ActivityGrupoPrincipal.class;
        } else if (view == linearPerfil) {
            actividad = Activity_PerfilPersonal.class;
        } else if (view == linearBuscar) {
            actividad = ActivityBuscarPrincipal.class;
        } else if (view == linearMultimedia) {
            actividad = ActivityPlayList.class;
        } else if (view == linearAjustes) {
            actividad = Activity_EditarPerfil.class;
        } else if (view == linearAcerca) {
            actividad = ActivityAcercaDe.class;
        } else if (view == linearCerrar) {
            cerrarSesion();
            actividad = activity_principal_login.class;
        }

        if (actividad != null) {
            moveActivity(actividad);
        }
    }

    private void moveActivity(Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    //Metodo para cerrar sesion
    private void cerrarSesion() {
        acceso.borrarToken();
        AppPreferences.resetFirstTimePreferences(context);
        // Regresar al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(context, activity_login.class);
        activity.startActivity(intent);
        activity.finish();
    }
}


