package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.ObtenerPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.informacionGeneralPlayListAstAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationGruposInfoClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.ArrayList;
import java.util.List;

public class ActivityAgregarPlayListMusica extends AppCompatActivity implements informacionGeneralPlayListAstAsyncTask.DataFetchListener{
    TextView agregarCanciones;
    ImageButton btnAtras;
    TextView nombrePlaylist;
    ImageView fotopPlaylist;

    private int idplaylist;
    ProgressDialog progressDialog;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private NavigationGruposInfoClickListener navigationClickListener;
    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_play_list_musica);
        agregarCanciones = (TextView) findViewById(R.id.textviewSiguienteAgregar);
        btnAtras = (ImageButton) findViewById(R.id.btn_PlayListInfoAtras);
        fotopPlaylist = (ImageView) findViewById(R.id.imageviewPlaylistInfoFoto);
        nombrePlaylist = (TextView) findViewById(R.id.textview_PlayInfoTitulo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idplaylist = getIntent().getIntExtra("idplaylist", 0);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));
        // Creación de una lista de elementos de playlistitem
        List<PlayListItem> playlistitemList = new ArrayList<>();

        // Crea y vincula el adaptador - playadapter
        PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
        //recyclerviewPlayLists.setAdapter(playAdapter);

        //Fetch data from the server
        String url = "https://phpclusters-156700-0.cloudclusters.net/obtenerPlayList.php";

        new informacionGeneralPlayListAstAsyncTask(this).execute(url, String.valueOf(idUsuario));
      new ObtenerPlayListAsyncTask(ActivityAgregarPlayListMusica.this, playAdapter, progressDialog).execute(String.valueOf(idUsuario));


        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.txtViewNavGrupos) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textviewSiguienteAgregar) {
                    actividad = Activity_SubirMusica.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        agregarCanciones.setOnClickListener(buttonClick);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }




    @Override
    public void onDataFetched(List<PlayListItem> dataList) {
        PlayListItem groupInfo = dataList.get(0);
        nombrePlaylist.setText(groupInfo.getItemName());
        fotopPlaylist.setImageBitmap(groupInfo.getImageResId());

    }
}