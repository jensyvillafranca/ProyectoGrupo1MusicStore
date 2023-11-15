package com.example.proyectogrupo1musicstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.BuscarIntegranteAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.NuevoGrupoIntegrantesAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class ActivityNuevoGrupoIntegrantes extends AppCompatActivity implements NuevoGrupoIntegrantesAsyncTask.DataFetchListener{

    RecyclerView lista;
    ImageButton botonSiguiente, botonAtras;
    TextView textviewSiguiente, textviewAtras;
    ProgressDialog progressDialog;
    List<Integer> selectedUserIds;
    CustomAdapterNuevoGrupoIntegrantes adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo_integrantes);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_NuevoGrupoIntegrantes);
        botonAtras = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesAtras);
        botonSiguiente = (ImageButton) findViewById(R.id.btn_NuevoGrupoIntegrantesSiguiente);
        textviewAtras = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotAtras);
        textviewSiguiente = (TextView) findViewById(R.id.textview_NuevoGrupoIntegrantesBotSiguiente);
        SearchView searchView = (SearchView) findViewById(R.id.searchview_NuevoGrupoIntegrantesBuscar);

        // Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/buscarSeguidores.php";
        String idUsuario = "1"; // Reemplazar por el idusuario - Motivos de prueba
        progressDialog.show();
        new NuevoGrupoIntegrantesAsyncTask(this).execute(url, idUsuario);

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Para controlar el boton Submit
                new BuscarIntegranteAsyncTask(ActivityNuevoGrupoIntegrantes.this, lista, adapter)
                        .execute("1", query); // Reemplazar "1" con el idusuario
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

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_NuevoGrupoIntegrantesAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_NuevoGrupoIntegrantesBotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
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
        adapter = new CustomAdapterNuevoGrupoIntegrantes(this, dataList);
        lista.setAdapter(adapter);
        selectedUserIds = adapter.getSelectedUserIds();
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    // Metodo Sobrecargado con la lista
    private void moveActivity(Class<?> actividad, List<Integer> selectedUserIds) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        intent.putIntegerArrayListExtra("selectedUserIds", new ArrayList<>(selectedUserIds));
        startActivity(intent);
    }
}