package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


import com.example.proyectogrupo1musicstore.Adapters.VideoPersonalAdapter;
import com.example.proyectogrupo1musicstore.Models.videoItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerVideosPersonalesAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityVideoSelection extends AppCompatActivity implements VideoPersonalAdapter.OnVideoItemClickListener{

    private int idUsuario;
    private ImageButton botAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_selection);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewVideoPersonal);
        botAtras = findViewById(R.id.btn_ChatUploadVideoAtras);
        idUsuario = getIntent().getIntExtra("idusuario", 0);

        // Crea y pone el adaptador
        List<videoItem> videoList = new ArrayList<>();
        VideoPersonalAdapter adapter = new VideoPersonalAdapter(videoList, this);
        recyclerView.setAdapter(adapter);

        // Configura el layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Ejecuta el AsyncTask para obtener los audios
        new obtenerVideosPersonalesAsyncTask(this, adapter)
                .execute(String.valueOf(idUsuario), String.valueOf(0));

        botAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onVideoItemClick(String url, String nombreArchivo) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedVideoUrl", url);
        resultIntent.putExtra("videoName", nombreArchivo);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}