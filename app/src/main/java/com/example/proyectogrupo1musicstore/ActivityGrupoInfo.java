package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Adapters.MusicaAdapter;
import com.example.proyectogrupo1musicstore.Adapters.VideoAdapter;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.Models.videoItem;

import java.util.ArrayList;
import java.util.List;

public class ActivityGrupoInfo extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, Grupos, Inicio;
    ImageView iconGrupos, iconInicio;
    RecyclerView recyclerViewIntegrantes, recyclerViewMusica, recyclerViewVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_info);

        // Declaración de variables
        recyclerViewIntegrantes = (RecyclerView) findViewById(R.id.recyclerviewIntegrantes);
        recyclerViewMusica = (RecyclerView) findViewById(R.id.recyclerviewMusica);
        recyclerViewVideos = (RecyclerView) findViewById(R.id.recyclerviewVideo);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGrupoInfo);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GrupoInfoMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GrupoInfoAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoInfoBotAtras);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);

        // Creación de una lista de elementos de integrantesItem
        List<integrantesItem> integrantesList = new ArrayList<>();

        // Creación de una lista de elementos de musicItem
        List<musicItem> musicList = new ArrayList<>();

        // Creación de una lista de elementos de videoItem
        List<videoItem> videoList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        IntegrantesAdapter integrantesAdapter = new IntegrantesAdapter(this, integrantesList);
        recyclerViewIntegrantes.setAdapter(integrantesAdapter);
        //musica
        MusicaAdapter musicaAdapter = new MusicaAdapter(this, musicList);
        recyclerViewMusica.setAdapter(musicaAdapter);
        //video
        VideoAdapter videoAdapter = new VideoAdapter(this, videoList);
        recyclerViewVideos.setAdapter(videoAdapter);

        // Configuracion del administrador de diseño - integrantes
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewIntegrantes.setLayoutManager(layoutManager);
        //musica
        LinearLayoutManager layoutManagerMusica = new LinearLayoutManager(this);
        layoutManagerMusica.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewMusica.setLayoutManager(layoutManagerMusica);
        //video
        LinearLayoutManager layoutManagerVideo = new LinearLayoutManager(this);
        layoutManagerVideo.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewVideos.setLayoutManager(layoutManagerVideo);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_GrupoInfoAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_GrupoInfoBotAtras) {
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