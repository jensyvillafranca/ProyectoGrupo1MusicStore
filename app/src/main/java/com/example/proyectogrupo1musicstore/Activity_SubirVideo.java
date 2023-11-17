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

public class Activity_SubirVideo extends AppCompatActivity {

    RecyclerView listas;
    DrawerLayout drawerLayouts;
    ImageButton openMenuButtons, botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos, Archivoss, Inicios;
    ImageView imageViewBuscarArchivos, iconoArchivos,iconInicios;
    CardView buscars,busquedaVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_video);

        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusicavideos);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layouts);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtrasBottons);
        textviewAtrass = (TextView) findViewById(R.id.textview_PrincipalbotAtrastexto);
        txtBuscarArchivos = (TextView) findViewById(R.id.txtbusquedaPrincipalActualizarvideos);
        imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusquedavideos);
        buscars = (CardView) findViewById(R.id.cardViewNavegacionMusicavideoos);
        busquedaVideos = (CardView) findViewById(R.id.cardViewBuscarArchivovideos);

        //Creacion de una lista de elementos de vistaArchivos
        List<vistaMusicaVideo> dataList = new ArrayList<>();
        dataList.add(new vistaMusicaVideo("La Lecon Particuliere (best part loop)", "Slowed Reverb", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo(" Be More ", "Stephen Sanchez", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("I Wanna Be Yours", "Arctic Monkeys", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("Yes To Heaven", "Lana Del Rey", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("Dayligth", "David Kushner", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("Mary On A Cross", "Ghosth", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("Total Eclipse Of The Heart", "Bonnie Tyler", R.drawable.iconovideos));
        dataList.add(new vistaMusicaVideo("Art Deco", "Lana Del Rey", R.drawable.iconovideos));


        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.cardViewNavegacionMusicavideoos) {
                    actividad = Activity_BuscarMusica.class;
                }
                if (view.getId() == R.id.cardViewBuscarArchivovideos) {
                    actividad = Activity_BuscarVideos.class;
                }
                if (view.getId() == R.id.btn_PrincipalAtrasBottons) {
                    actividad = Activity_CrearPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        buscars.setOnClickListener(buttonClick);
        busquedaVideos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);

    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

}