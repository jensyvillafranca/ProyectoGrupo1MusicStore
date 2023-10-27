package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActivityUnirseGrupo extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, Grupos, Inicio;

    ImageView iconGrupos, iconInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_grupo);

        // Inicialización de vistas y elementos del diseño
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGruposUnirse);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposUnirseMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposUnirseAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoUnirsebotAtras);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_GruposUnirseAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_GrupoUnirsebotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
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
        };

        // Asigna los listeners a los botones de "Atrás"
        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}