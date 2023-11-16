package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActivityPantallaPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton openMenuButton;
    TextView Grupos, Inicio, CerrarSesion;
    ImageView iconGrupos, iconInicio, multimedia, iconCerrarSesion;
    private token acceso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openMenuButton = (ImageButton) findViewById(R.id.btn_PrincipalDesplegable);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);
        multimedia = (ImageView) findViewById(R.id.iconNavMultimedia);

        /*Variables para cerrar sesión*/
        iconCerrarSesion = (ImageView) findViewById(R.id.iconCerrarSesion);
        CerrarSesion = (TextView) findViewById(R.id.txtviewCerrarSesion);
        acceso = new token(this);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
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
                if (view.getId() == R.id.iconNavMultimedia){
                    actividad = ActivityPlayList.class;
                }
                if (view.getId() == R.id.iconCerrarSesion){
                    cerrarSesion();
                    actividad = activity_principal_login.class;
                }
                if (view.getId() == R.id.txtviewCerrarSesion){
                    cerrarSesion();
                    actividad = activity_principal_login.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
        multimedia.setOnClickListener(buttonClick);
        CerrarSesion.setOnClickListener(buttonClick);
        iconCerrarSesion.setOnClickListener(buttonClick);

        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    private void cerrarSesion() {
        acceso.borrarToken();
        // Regresar al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
        finish();
    }
}