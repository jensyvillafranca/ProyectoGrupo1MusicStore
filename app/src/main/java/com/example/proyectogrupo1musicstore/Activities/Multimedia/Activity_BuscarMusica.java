package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.BuscarAudiosAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.ArrayList;
import java.util.List;

public class Activity_BuscarMusica extends AppCompatActivity {


    RecyclerView listas;
    DrawerLayout drawerLayouts;
    ImageButton botonAtrasss;
    TextView textviewAtrass, textviewGruposBuscarss;
    EditText txtGruposBuscarss;
    ImageView imgBuscarss, imgBuscar2ss ;
    CardView buscarss;

    private NavigationClickListener navigationClickListener;

    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_musica);


        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_AudiosBuscarMusica);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layoutGruposBuscarMusica);
        botonAtrasss = (ImageButton) findViewById(R.id.btn_AudiosBuscarAtras);
        textviewAtrass = (TextView) findViewById(R.id.textview_AudioBuscarbotAtras);
        textviewGruposBuscarss = (TextView) findViewById(R.id.txtAudiosBuscarBuscarMusica);
        txtGruposBuscarss = (EditText) findViewById(R.id.editTextAudiosBuscar);
        imgBuscarss = (ImageView) findViewById(R.id.imageViewAudiosBuscarMusica);
        imgBuscar2ss = (ImageView) findViewById(R.id.imageViewAudiosBusquedaBuscar2);
        buscarss = (CardView) findViewById(R.id.cardViewNavegacionVideo);

        // Inicializa listener para elementos
        navigationClickListener = new NavigationClickListener(this, this);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        //Creacion de una lista de elementos de vistaArchivos
        List<buscarAudioMusica> dataList = new ArrayList<>();

        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

        try{
            String busqueda;
            busqueda = getIntent().getStringExtra("busqueda");
            if(!busqueda.isEmpty()){
                txtGruposBuscarss.setText(busqueda);
                String query = txtGruposBuscarss.getText().toString();
                new BuscarAudiosAsyncTask(Activity_BuscarMusica.this, listas, adapter)
                        .execute(String.valueOf(idUsuario), query);
            }
        }catch (Exception e){
            Log.e("Error Parameter", "No se encontro un parametro");
        }

        botonAtrasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        textviewGruposBuscarss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el textview y muestra el edittext
                textviewGruposBuscarss.setVisibility(View.GONE);
                txtGruposBuscarss.setVisibility(View.VISIBLE);
                imgBuscarss.setVisibility(View.GONE);
                imgBuscar2ss.setVisibility(View.VISIBLE);
                txtGruposBuscarss.requestFocus();

                // Mostrar el Teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtGruposBuscarss, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        //Listener para manerjar la visibilidad del boton de busqueda
        txtGruposBuscarss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el edittext y muestra el textview
                textviewGruposBuscarss.setVisibility(View.VISIBLE);
                txtGruposBuscarss.setVisibility(View.GONE);
                imgBuscarss.setVisibility(View.VISIBLE);
                imgBuscar2ss.setVisibility(View.GONE);
                // Cierra el teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        //Listener para manejar el cierre del teclado con el boton de enter
        txtGruposBuscarss.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //obtiene el texto de busqueda
                    String query = txtGruposBuscarss.getText().toString();

                    //llama el asynctask
                    new BuscarAudiosAsyncTask(Activity_BuscarMusica.this, listas, adapter)
                            .execute(String.valueOf(idUsuario), query);

                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        imgBuscar2ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtiene el texto de busqueda
                String query = txtGruposBuscarss.getText().toString();

                //llama el asynctask
                new BuscarAudiosAsyncTask(Activity_BuscarMusica.this, listas, adapter)
                        .execute(String.valueOf(idUsuario), query);
            }
        });

    }

    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}