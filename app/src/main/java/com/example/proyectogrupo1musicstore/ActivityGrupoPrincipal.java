package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapter;
import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;

import java.util.ArrayList;
import java.util.List;

public class ActivityGrupoPrincipal extends AppCompatActivity {

    // Declaración de variables
    RecyclerView lista;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, txtGruposBuscar, txtNuevoGrupo, Grupos, Inicio;
    ImageView imageviewGruposBuscar, imageviewNuevoGrupo, iconGrupos, iconInicio;
    CardView buscar, nuevoGrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_principal);

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_GruposPrincipal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposPrincipalMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposPrincipalAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoPrincipalbotAtras);
        txtGruposBuscar = (TextView) findViewById(R.id.txtGruposPrincipalBuscarGrupo);
        txtNuevoGrupo = (TextView) findViewById(R.id.txtGruposPrincipalNuevoGrupo);
        imageviewGruposBuscar = (ImageView) findViewById(R.id.imageviewGruposPrincipalBuscar);
        imageviewNuevoGrupo = (ImageView) findViewById(R.id.imageviewGruposPrincipalNuevoGrupo);
        buscar = (CardView) findViewById(R.id.cardViewGruposPrincipalBuscar);
        nuevoGrupo = (CardView) findViewById(R.id.cardViewGruposPrincipalNuevo);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);

        // Creación de una lista de elementos de vistaDeGrupo
        List<vistaDeGrupo> dataList = new ArrayList<>();
        dataList.add(new vistaDeGrupo("Grupo 1", "Creado por mi", "Integrantes: 10", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 2", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 3", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 4", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 5", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 6", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 7", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));
        dataList.add(new vistaDeGrupo("Grupo 8", "Creado por mi", "Integrantes: 5", R.drawable.logopantallaprincipal));

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        lista.setLayoutManager(layoutManager);
        CustomAdapter adapter = new CustomAdapter(this, dataList);
        lista.setAdapter(adapter);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId()==R.id.btn_GruposPrincipalAtras) {
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (view.getId()==R.id.textview_GrupoPrincipalbotAtras){
                    actividad = ActivityPantallaPrincipal.class;
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
        txtGruposBuscar.setOnClickListener(buttonClick);
        imageviewGruposBuscar.setOnClickListener(buttonClick);
        buscar.setOnClickListener(buttonClick);
        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
        txtGruposBuscar.setOnClickListener(buttonClick);
        imageviewGruposBuscar.setOnClickListener(buttonClick);
        nuevoGrupo.setOnClickListener(buttonClick);
    }

    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}