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

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityVerTodosIntegrantes;
import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.ObtenerPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.informacionGeneralPlayListAstAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.obtenerIntegrantesGrupoAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class ActivityVerPlayList extends AppCompatActivity {
    ImageButton botonAtrass;
    RecyclerView recyclerviewvertodoPlayList;
    ProgressDialog progressDialog;
    private int idplaylist;

    private final String tipo = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_play_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idplaylist = 2;
        recyclerviewvertodoPlayList  = (RecyclerView) findViewById(R.id.recyclerview_vertodoPlayList);
        botonAtrass = (ImageButton) findViewById(R.id.btn_vertodosPlayListAtras);

        // Creación de una lista de elementos de integrantesItem
        List<PlayListItem> playlistitemList = new ArrayList<>();
        // Crea y vincula el adaptador - playadapter
        PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
        recyclerviewvertodoPlayList.setAdapter(playAdapter);

        //Configuracion del administrador de diseño playlist
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerviewvertodoPlayList.setLayoutManager(layoutManager);

        new ObtenerPlayListAsyncTask(ActivityVerPlayList.this, playAdapter, progressDialog)
                .execute(String.valueOf(idplaylist), tipo);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;

                if (view.getId() == R.id.btn_vertodosPlayListAtras) {
                    actividad = ActivityPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        botonAtrass.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        intent.putExtra("idplaylist", idplaylist);
        startActivity(intent);

    }

}