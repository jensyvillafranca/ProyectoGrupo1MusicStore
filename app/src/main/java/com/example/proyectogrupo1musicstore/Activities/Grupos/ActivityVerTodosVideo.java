package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.VideoAdapter;
import com.example.proyectogrupo1musicstore.Models.videoItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerVideosGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityVerTodosVideo extends AppCompatActivity {

    ImageButton botonAtras;
    TextView textviewAtras;
    RecyclerView recyclerViewVideo;
    ProgressDialog progressDialog;
    private int idgrupo;
    private final String tipo = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todos_video);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idgrupo = getIntent().getIntExtra("idgrupo", 0);

        recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerview_vertodoVideo);
        botonAtras = (ImageButton) findViewById(R.id.btn_vertodosVideoAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_vertodosVideoBotAtras);

        // Creación de una lista de elementos
        List<videoItem> videoList = new ArrayList<>();

        // Crea y vincula el adaptador
        VideoAdapter videoAdapter = new VideoAdapter(this, videoList);
        recyclerViewVideo.setAdapter(videoAdapter);

        //Configuracion del administrador de diseño
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewVideo.setLayoutManager(layoutManager);

        new obtenerVideosGrupoAsyncTask(ActivityVerTodosVideo.this, videoAdapter, progressDialog)
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