package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;

import java.util.ArrayList;
import java.util.List;

public class ActivityNuevoGrupoIntegrantes extends AppCompatActivity {

    RecyclerView lista;
    ImageButton botonSiguiente, botonAtras;
    TextView textviewSiguiente, textviewAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo_integrantes);

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_NuevoGrupoIntegrantes);
        botonAtras = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesAtras);
        botonSiguiente = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesSiguiente);
        textviewAtras = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotAtras);
        textviewSiguiente = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotSiguiente);

        // Creación de una lista de elementos de vistaDeNuevoGrupo
        List<vistaDeNuevoGrupo> dataList = new ArrayList<>();

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        CustomAdapterNuevoGrupoIntegrantes adapter = new CustomAdapterNuevoGrupoIntegrantes(this, dataList);
        lista.setAdapter(adapter);

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_NuevoGrupoIntegrantesAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_NuevoGrupoIntegrantesBotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_NuevoGrupoIntegrantesBotSiguiente) {
                    actividad = ActivityNuevoGrupoDetalles.class;
                }
                if (view.getId() == R.id.btn_NuevoGrupoIntegrantesSiguiente) {
                    actividad = ActivityNuevoGrupoDetalles.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        // Asigna los listeners a los botones de "Atrás"
        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
        botonSiguiente.setOnClickListener(buttonClick);
        textviewSiguiente.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}