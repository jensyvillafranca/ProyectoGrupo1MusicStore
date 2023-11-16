package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.obtenerIntegrantesGrupoAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class ActivityVerTodosIntegrantes extends AppCompatActivity {

    ImageButton botonAtras;
    TextView textviewAtras;
    RecyclerView recyclerViewIntegrantes;
    ProgressDialog progressDialog;
    private int idgrupo;
    private final String tipo = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todos_integrantes);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idgrupo = getIntent().getIntExtra("idgrupo", 0);

        recyclerViewIntegrantes = (RecyclerView) findViewById(R.id.recyclerview_vertodoIntegrantes);
        botonAtras = (ImageButton) findViewById(R.id.btn_vertodosIntegrantesAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_vertodosIntegrantesBotAtras);

        // Creación de una lista de elementos de integrantesItem
        List<integrantesItem> integrantesList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        IntegrantesAdapter integrantesAdapter = new IntegrantesAdapter(this, integrantesList);
        recyclerViewIntegrantes.setAdapter(integrantesAdapter);

        //Configuracion del administrador de diseño - integrantes
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewIntegrantes.setLayoutManager(layoutManager);

        new obtenerIntegrantesGrupoAsyncTask(ActivityVerTodosIntegrantes.this, integrantesAdapter, progressDialog)
                .execute(String.valueOf(idgrupo), tipo);

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_vertodosIntegrantesAtras) {
                    actividad = ActivityGrupoInfo.class;
                }
                if (view.getId() == R.id.textview_vertodosIntegrantesBotAtras) {
                    actividad = ActivityGrupoInfo.class;
                }
                if (actividad != null) {
                    moveActivity(actividad, idgrupo);
                }
            }
        };

        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad, int idgrupo) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        intent.putExtra("idgrupo", idgrupo);
        startActivity(intent);
    }
}