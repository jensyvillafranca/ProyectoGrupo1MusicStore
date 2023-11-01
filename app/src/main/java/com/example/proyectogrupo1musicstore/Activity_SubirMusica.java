package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapter;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.vistaMusicaVideo;

import java.util.ArrayList;
import java.util.List;

public class Activity_SubirMusica extends AppCompatActivity {
    RecyclerView listas;
    DrawerLayout drawerLayouts;
    ImageButton openMenuButtons, botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos, Archivoss, Inicios;
    ImageView imageViewBuscarArchivos, iconoArchivos,iconInicios;
    CardView buscars, videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_musica);

        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusica);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layout);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtras);
        textviewAtrass = (TextView) findViewById(R.id.textview_GrupoPrincipalbotAtras);
        txtBuscarArchivos = (TextView) findViewById(R.id.BuscarArchivo);
        imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusqueda);
        buscars = (CardView) findViewById(R.id.cardViewBuscarArchivo);
        videos = (CardView) findViewById(R.id.cardViewNavegacionVideo);

        //Creacion de una lista de elementos de vistaArchivos
        List<vistaMusicaVideo> dataList = new ArrayList<>();
        dataList.add(new vistaMusicaVideo("La Lecon Particuliere (best part loop)", "Slowed Reverb", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo(" Be More ", "Stephen Sanchez", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("I Wanna Be Yours", "Arctic Monkeys", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("Yes To Heaven", "Lana Del Rey", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("Dayligth", "David Kushner", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("Mary On A Cross", "Ghosth", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("Total Eclipse Of The Heart", "Bonnie Tyler", R.drawable.iconomusicas));
        dataList.add(new vistaMusicaVideo("Art Deco", "Lana Del Rey", R.drawable.iconomusicas));


        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.cardViewNavegacionVideo) {
                    actividad = Activity_SubirVideo.class;
                }
                if (view.getId() == R.id.cardViewBuscarArchivo) {
                    actividad = Activity_BuscarMusica.class;
                }
                if (view.getId() == R.id.btn_PrincipalAtras) {
                    actividad = Activity_CrearPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        buscars.setOnClickListener(buttonClick);
        videos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);
    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}