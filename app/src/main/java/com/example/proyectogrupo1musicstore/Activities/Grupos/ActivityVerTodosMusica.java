package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.MusicaAdapter;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerAudiosGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityVerTodosMusica extends AppCompatActivity {

    ImageButton botonAtras;
    TextView textviewAtras;
    RecyclerView recyclerViewMusica;
    ProgressDialog progressDialog;
    private int idgrupo;
    private final String tipo = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vert_todos_musica);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idgrupo = getIntent().getIntExtra("idgrupo", 0);

        recyclerViewMusica = (RecyclerView) findViewById(R.id.recyclerview_vertodoMusica);
        botonAtras = (ImageButton) findViewById(R.id.btn_vertodosMusicaAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_vertodosMusicaBotAtras);

        // Creación de una lista de elementos
        List<musicItem> musicaList = new ArrayList<>();

        // Crea y vincula el adaptador
        MusicaAdapter musicaAdapter = new MusicaAdapter(this, musicaList);
        recyclerViewMusica.setAdapter(musicaAdapter);

        //Configuracion del administrador de diseño
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewMusica.setLayoutManager(layoutManager);

        new obtenerAudiosGrupoAsyncTask(ActivityVerTodosMusica.this, musicaAdapter, progressDialog)
                .execute(String.valueOf(idgrupo), tipo);

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textviewAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}