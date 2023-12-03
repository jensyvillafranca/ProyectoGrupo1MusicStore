package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.proyectogrupo1musicstore.Adapters.AudioPersonalAdapter;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerAudiosPersonalesAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityAudioSelection extends AppCompatActivity implements AudioPersonalAdapter.OnAudioItemClickListener{

    private int idUsuario;
    private ImageButton botAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_selection);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAudioPersonal);
        botAtras = findViewById(R.id.btn_ChatUploadAtras);
        idUsuario = getIntent().getIntExtra("idusuario", 0);

        // Crea y pone el adaptador
        List<musicItem> musicList = new ArrayList<>();
        AudioPersonalAdapter adapter = new AudioPersonalAdapter(musicList, this);
        recyclerView.setAdapter(adapter);

        // Configura el layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Ejecuta el AsyncTask para obtener los audios
        new obtenerAudiosPersonalesAsyncTask(this, adapter)
                .execute(String.valueOf(idUsuario), String.valueOf(0));

        botAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onAudioItemClick(String url, String nombreArchivo) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedAudioUrl", url);
        resultIntent.putExtra("audioName", nombreArchivo);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}