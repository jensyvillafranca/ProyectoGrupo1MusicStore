package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterPlay;
import com.example.proyectogrupo1musicstore.Models.vistadeplaylist;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.FetchDataAsyncModiPlayList;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.List;

public class ActivityModificarPlayList extends AppCompatActivity implements FetchDataAsyncModiPlayList.DataFetchListener {

    DrawerLayout drawerLayout;
    RecyclerView modilistaplaylist;
    ImageButton openMenuButton, btnAtras;
    TextView ingresarModificar;
    ProgressDialog progressDialog;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_play_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

       // ingresarModificar = (TextView) findViewById(R.id.modificarPlayList);
        btnAtras = (ImageButton) findViewById(R.id.btn_ModificarPlayListlAtras);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutsModificacionPlayLisy);
        modilistaplaylist = (RecyclerView) findViewById(R.id.recyclerview_ModificacionPlayList);

        String url = "https://phpclusters-156700-0.cloudclusters.net/modiPlayList.php";
        //Remplazar por el idusuario - motivos de pruebas
        progressDialog.show();
        new FetchDataAsyncModiPlayList(this, progressDialog).execute(url, String.valueOf(idUsuario));


        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        modilistaplaylist.setLayoutManager(layoutManager);




        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_ModificarPlayListlAtras) {
                    actividad = ActivityPlay.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

      //  ingresarModificar.setOnClickListener(buttonClick);
        btnAtras.setOnClickListener(buttonClick);


    }

    public void onDataFetched(List<vistadeplaylist> dataList) {
        // Muestra el Recycle view con la nueva informacion
        progressDialog.dismiss(); // Esconde el spinner de carga
        CustomAdapterPlay adapter = new CustomAdapterPlay(this, dataList, idUsuario, modilistaplaylist);
        modilistaplaylist.setAdapter(adapter);
    }

    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}