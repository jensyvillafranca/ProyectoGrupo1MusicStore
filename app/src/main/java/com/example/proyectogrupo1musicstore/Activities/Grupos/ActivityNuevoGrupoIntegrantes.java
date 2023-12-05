package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.BuscarIntegranteAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.NuevoGrupoIntegrantesAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.ArrayList;
import java.util.List;

public class ActivityNuevoGrupoIntegrantes extends AppCompatActivity implements NuevoGrupoIntegrantesAsyncTask.DataFetchListener {

    RecyclerView lista;
    ImageButton botonSiguiente, botonAtras;
    TextView textviewSiguiente, textviewAtras;
    ProgressDialog progressDialog;
    List<Integer> selectedUserIds;
    CustomAdapterNuevoGrupoIntegrantes adapter;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo_integrantes);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        selectedUserIds = new ArrayList<>();

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_NuevoGrupoIntegrantes);
        botonAtras = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesAtras);
        botonSiguiente = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesSiguiente);
        textviewAtras = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotAtras);
        textviewSiguiente = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotSiguiente);
        SearchView searchView = (SearchView) findViewById(R.id.searchview_NuevoGrupoIntegrantesBuscar);

        // Fetch data from the server
        String url = "https://phpclusters-156700-0.cloudclusters.net/buscarSeguidores.php";
        progressDialog.show();
        new NuevoGrupoIntegrantesAsyncTask(this, progressDialog).execute(url, String.valueOf(idUsuario));

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Para controlar el boton Submit
                new BuscarIntegranteAsyncTask(ActivityNuevoGrupoIntegrantes.this, lista, adapter)
                        .execute(String.valueOf(idUsuario), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Llama la funcion AsyncTask con el contenido de la busqueda
                /*new BuscarIntegranteAsyncTask(ActivityNuevoGrupoIntegrantes.this, lista, adapter)
                        .execute("1", newText); // Reemplazar "1" con el idusuario*/
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Reload all followers using NuevoGrupoIntegrantesAsyncTask
                String url = "https://phpclusters-156700-0.cloudclusters.net/buscarSeguidores.php";
                progressDialog.show();
                new NuevoGrupoIntegrantesAsyncTask(ActivityNuevoGrupoIntegrantes.this, progressDialog).execute(url, String.valueOf(idUsuario));
                return false;
            }
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_NuevoGrupoIntegrantesAtras) {
                    finish();
                }
                if (view.getId() == R.id.textview_NuevoGrupoIntegrantesBotAtras) {
                    finish();
                }
                if (view.getId() == R.id.textview_NuevoGrupoIntegrantesBotSiguiente) {
                    actividad = ActivityNuevoGrupoDetalles.class;
                }
                if (view.getId() == R.id.btn_NuevoGrupoIntegrantesSiguiente) {
                    actividad = ActivityNuevoGrupoDetalles.class;
                }
                if (actividad != null) {
                    if (actividad == ActivityNuevoGrupoDetalles.class) {
                        moveActivity(actividad, selectedUserIds);
                    } else {
                        moveActivity(actividad);
                    }
                }
            }
        };

        // Asigna los listeners a los botones de "Atrás"
        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
        botonSiguiente.setOnClickListener(buttonClick);
        textviewSiguiente.setOnClickListener(buttonClick);
    }

    @Override
    public void onDataFetched(List<vistaDeNuevoGrupo> dataList) {
        // Muestra el Recycle view con la nueva informacion
        progressDialog.dismiss(); // Esconde el spinner de carga
        adapter = new CustomAdapterNuevoGrupoIntegrantes(this, dataList, idUsuario, 0, lista);
        lista.setAdapter(adapter);
        selectedUserIds = adapter.getSelectedUserIds();
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    // Metodo Sobrecargado con la lista
    private void moveActivity(Class<?> actividad, List<Integer> selectedUserIds) {
        Log.e("IdUsuario: ", String.valueOf(idUsuario));
        selectedUserIds.add(idUsuario);
        Intent intent = new Intent(getApplicationContext(), actividad);
        intent.putIntegerArrayListExtra("selectedUserIds", new ArrayList<>(selectedUserIds));
        startActivity(intent);
    }
}