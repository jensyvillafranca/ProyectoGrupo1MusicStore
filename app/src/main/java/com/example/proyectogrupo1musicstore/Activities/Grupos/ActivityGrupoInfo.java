package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Adapters.MusicaAdapter;
import com.example.proyectogrupo1musicstore.Adapters.VideoAdapter;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoGeneral;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.Models.videoItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.InfomacionGeneralGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.obtenerAudiosGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.obtenerIntegrantesGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.obtenerVideosGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityGrupoInfo extends AppCompatActivity implements InfomacionGeneralGrupoAsyncTask.DataFetchListener {

    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, Grupos, Inicio, nombreGrupo, textviewNumeroIntegrantes, textviewNumeroAudio, textviewNumeroVideo, verTodoIntegrantes, verTodoMusica, verTodoVideos;
    ImageView iconGrupos, iconInicio, fotoGrupo;
    RecyclerView recyclerViewIntegrantes, recyclerViewMusica, recyclerViewVideos;
    private int idgrupo;
    private final String tipo = "0";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_info);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idgrupo = getIntent().getIntExtra("idgrupo", 0);

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
        fotoGrupo = (ImageView) findViewById(R.id.imageviewGrupoInfoFoto);
        nombreGrupo = (TextView) findViewById(R.id.textview_GrupoInfoTitulo);
        textviewNumeroIntegrantes = (TextView) findViewById(R.id.textviewIntegrantesTitle);
        textviewNumeroAudio = (TextView) findViewById(R.id.textviewMusicaTitle);
        textviewNumeroVideo = (TextView) findViewById(R.id.textviewVideoTitle);
        verTodoIntegrantes = (TextView) findViewById(R.id.textviewVerTodoIntegrantes);
        verTodoMusica = (TextView) findViewById(R.id.textviewVerTodoMusica);
        verTodoVideos = (TextView) findViewById(R.id.textviewVerTodoVideo);

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

        //Configuracion del administrador de diseño - integrantes
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

        // Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerInfoGrupo.php";
        progressDialog.show();
        new InfomacionGeneralGrupoAsyncTask(this).execute(url, String.valueOf(idgrupo));
        new obtenerIntegrantesGrupoAsyncTask(ActivityGrupoInfo.this, integrantesAdapter, progressDialog)
                .execute(String.valueOf(idgrupo), tipo);
        new obtenerAudiosGrupoAsyncTask(ActivityGrupoInfo.this, musicaAdapter, progressDialog)
                .execute(String.valueOf(idgrupo), tipo);
        new obtenerVideosGrupoAsyncTask(ActivityGrupoInfo.this, videoAdapter, progressDialog)
                .execute(String.valueOf(idgrupo), tipo);


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
                if (view.getId() == R.id.textviewVerTodoIntegrantes){
                    actividad = ActivityVerTodosIntegrantes.class;
                }
                if (view.getId() == R.id.textviewVerTodoMusica){
                    actividad = ActivityVerTodosMusica.class;
                }
                if (view.getId() == R.id.textviewVerTodoVideo){
                    actividad = ActivityVerTodosVideo.class;
                }
                if (actividad != null) {
                    moveActivity(actividad, idgrupo);
                }
            }
        };

        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
        verTodoIntegrantes.setOnClickListener(buttonClick);
        verTodoMusica.setOnClickListener(buttonClick);
        verTodoVideos.setOnClickListener(buttonClick);
    }

    @Override
    public void onDataFetched(List<informacionGrupoGeneral> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            informacionGrupoGeneral groupInfo = dataList.get(0);

            nombreGrupo.setText(groupInfo.getNombre());
            fotoGrupo.setImageBitmap(groupInfo.getFoto());
            textviewNumeroIntegrantes.setText("Integrantes: "+groupInfo.getNumeroMiembros());
            textviewNumeroAudio.setText("Audio: "+groupInfo.getNumeroMusica());
            textviewNumeroVideo.setText("Videos: "+groupInfo.getNumeroVideos());
        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    private void moveActivity(Class<?> actividad, int idgrupo) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        intent.putExtra("idgrupo", idgrupo);
        startActivity(intent);
    }
}