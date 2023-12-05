package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.buscarAudioMusica;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_BuscarVideos extends AppCompatActivity {

    RecyclerView listas;
    DrawerLayout drawerLayouts;
    ImageButton openMenuButton, botonAtrasss;
    TextView textviewAtrass, textviewGruposBuscarss, Grupos, Inicio;
    EditText txtGruposBuscarss;
    ImageView imgBuscarss, imgBuscar2ss, iconGrupos, iconInicio;
    CardView buscarss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_videos);

        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_BuscaVideosBusqueda);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layoutsBuscarVideosBusquedasupe);
        botonAtrasss = (ImageButton) findViewById(R.id.btn_BuscarAtrasVideos);
        textviewAtrass = (TextView) findViewById(R.id.textview_BuscarbotAtrasVideos);
        textviewGruposBuscarss = (TextView) findViewById(R.id.txtBuscarBuscarVideos);
        txtGruposBuscarss = (EditText) findViewById(R.id.editTextBuscarVideosBusqueda);
        imgBuscarss = (ImageView) findViewById(R.id.imageViewBuscarVideoBuscador);
        imgBuscar2ss = (ImageView) findViewById(R.id.imageViewBuscarVideos2);
        buscarss = (CardView) findViewById(R.id.cardViewNavegacionMusicavideoos);

        //Creacion de una lista de elementos de vistaArchivos
        List<buscarAudioMusica> dataList = new ArrayList<>();

        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_BuscarAtrasVideos) {
                    actividad = Activity_SubirVideo.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        //Listener para manejar el cierre del teclado con el boton de enter
        txtGruposBuscarss.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        botonAtrasss.setOnClickListener(buttonClick);
    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}