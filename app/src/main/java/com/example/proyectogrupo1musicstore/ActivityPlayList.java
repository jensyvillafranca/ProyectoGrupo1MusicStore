package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Models.informacionGeneralPlayList;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoGeneral;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.ObtenerPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.informacionGeneralPlayListAstAsyncTask;


import java.util.ArrayList;
import java.util.List;

public class ActivityPlayList extends AppCompatActivity implements informacionGeneralPlayListAstAsyncTask.DataFetchListener {
    Button CrearPlays;
    TextView txtSiguiente, nombreplay,textviewNumeroPlay,txtSiguienteVerTodo;
    RecyclerView recyclerviewPlayLists, recyclerviewMusicasFavoritass;
    ImageView fotoPlay;

    private int idplaylist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);



        idplaylist = getIntent().getIntExtra("idplaylist",0);

        // Declaración de variables
        recyclerviewPlayLists = (RecyclerView) findViewById(R.id.recyclerviewPlayList);
        recyclerviewMusicasFavoritass = (RecyclerView) findViewById(R.id.recyclerviewMusicasFavoritas);
        textviewNumeroPlay = (TextView) findViewById(R.id.textviewPlayList);
        txtSiguiente = (TextView) findViewById(R.id.txtPrincipal);
        txtSiguienteVerTodo  = (TextView) findViewById(R.id.textviewVerTodoMusica);

        // Creación de una lista de elementos de integrantesItem
        List<PlayListItem> playlistitemList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
        recyclerviewPlayLists.setAdapter(playAdapter);

        //Configuracion del administrador de diseño - integrantes
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerviewPlayLists.setLayoutManager(layoutManager);

        //Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerPlayList.php";

        new informacionGeneralPlayListAstAsyncTask(this).execute(url, String.valueOf(idplaylist));
        new ObtenerPlayListAsyncTask(ActivityPlayList.this, playAdapter, progressDialog)
              .execute(String.valueOf(idplaylist));

//        new ObtenerPlayListAsyncTask(ActivityPlayList.this, playAdapter, progressDialog)
  //              .execute(String.valueOf(idplaylist));


        CrearPlays = (Button) findViewById(R.id.btnCrear);
        CrearPlays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creacion = new Intent(getApplicationContext(),Activity_CrearPlayList.class);
                startActivity(creacion);
            }
        });

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.txtPrincipal) {
                    actividad = ActivityArchivosPersonales.class;
                }
                if (view.getId() == R.id.textviewVerTodoMusica) {
                    actividad = ActivityVerPlayList.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };



        //Listener para manejar el cierre del teclado con el boton de enter
        txtSiguiente.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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



        txtSiguienteVerTodo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        txtSiguiente.setOnClickListener(buttonClick);
        txtSiguienteVerTodo.setOnClickListener(buttonClick);
    }


    @Override
    public void onDataFetched(List<informacionGeneralPlayList> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            informacionGeneralPlayList playInfon = dataList.get(0);

            nombreplay.setText(playInfon.getNombre());
            fotoPlay.setImageBitmap(playInfon.getFoto());
            textviewNumeroPlay.setText("PlayList: "+playInfon.getNumeroMusica());

        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }




}