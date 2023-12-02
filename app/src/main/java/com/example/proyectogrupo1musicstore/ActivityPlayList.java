package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.ObtenerPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.informacionGeneralPlayListAstAsyncTask;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;


import java.util.ArrayList;
import java.util.List;

public class ActivityPlayList extends AppCompatActivity implements informacionGeneralPlayListAstAsyncTask.DataFetchListener {
    Button CrearPlays;
    TextView txtSiguiente, nombreplay,textviewNumeroPlay,txtSiguienteVerTodo, mostrarNombreUsuario;
    RecyclerView recyclerviewPlayLists, recyclerviewMusicasFavoritass;
    ImageView fotoPlay;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);

    private int idplaylist;
   // private int idUsuario;

    private int idUsuario = 2;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        idplaylist = getIntent().getIntExtra("idplaylist", 0);

        // Declaración de variables
        recyclerviewPlayLists = (RecyclerView) findViewById(R.id.recyclerviewPlayList);
        recyclerviewMusicasFavoritass = (RecyclerView) findViewById(R.id.recyclerviewMusicasFavoritas);
        textviewNumeroPlay = (TextView) findViewById(R.id.textviewPlayList);
        txtSiguiente = (TextView) findViewById(R.id.txtPrincipal);
        txtSiguienteVerTodo  = (TextView) findViewById(R.id.textviewVerTodoMusicas);
        openMenuButton = (ImageButton) findViewById(R.id.btn_Principal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutPlayList);
       // mostrarNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuario);

        // Creación de una lista de elementos de playlistitem
        List<PlayListItem> playlistitemList = new ArrayList<>();

        // Crea y vincula el adaptador - playadapter
        PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
        recyclerviewPlayLists.setAdapter(playAdapter);

        //Configuracion del administrador de diseño - platlist
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerviewPlayLists.setLayoutManager(layoutManager);

        //Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerPlayList.php";

        new informacionGeneralPlayListAstAsyncTask(this).execute(url, String.valueOf(idUsuario));
        new ObtenerPlayListAsyncTask(ActivityPlayList.this, playAdapter, progressDialog).execute(String.valueOf(idUsuario));

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menusss));
        });
//        new ObtenerPlayListAsyncTask(ActivityPlayList.this, playAdapter, progressDialog)
  //              .execute(String.valueOf(idplaylist));


        CrearPlays = (Button) findViewById(R.id.btnCrear);
        CrearPlays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creacion = new Intent(getApplicationContext(),ActivityPlay.class);
                startActivity(creacion);
            }
        });

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.txtPrincipal) {
                    actividad = Activity_SubirMusica.class;
                }
                if (view.getId() == R.id.textviewVerTodoMusicas) {
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
    public void onDataFetched(List<PlayListItem> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            PlayListAdapter adapter = new PlayListAdapter(this, dataList);
            recyclerviewPlayLists.setAdapter(adapter);

        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }




}