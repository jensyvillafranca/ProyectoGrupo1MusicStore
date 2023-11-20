package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.app.ProgressDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterBuscarGrupos;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.BuscarGruposAsyncTask;
import com.example.proyectogrupo1musicstore.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityGruposBuscar extends AppCompatActivity {

    // Declaración de variables
    RecyclerView lista;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, textviewGruposBuscar, Grupos, Inicio;
    EditText txtGruposBuscar;
    ImageView imgBuscar, imgBuscar2, iconGrupos, iconInicio;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos_buscar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_GruposBuscar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGruposBuscar);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposBuscarMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposBuscarAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoBuscarbotAtras);
        textviewGruposBuscar = (TextView) findViewById(R.id.txtGruposBuscarBuscarGrupo);
        txtGruposBuscar = (EditText) findViewById(R.id.editTextGruposBuscar);
        imgBuscar = (ImageView) findViewById(R.id.imageViewGruposBuscar);
        imgBuscar2 = (ImageView) findViewById(R.id.imageViewGruposBuscar2);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);

        // Creación de una lista de elementos de vistaDeGrupo
        List<buscarGrupo> dataList = new ArrayList<>();

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        CustomAdapterBuscarGrupos adapter = new CustomAdapterBuscarGrupos(this, dataList);
        lista.setAdapter(adapter);

        //Listener para manerjar la visibilidad del boton de busqueda
        textviewGruposBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el textview y muestra el edittext
                textviewGruposBuscar.setVisibility(View.GONE);
                txtGruposBuscar.setVisibility(View.VISIBLE);
                imgBuscar.setVisibility(View.GONE);
                imgBuscar2.setVisibility(View.VISIBLE);
                txtGruposBuscar.requestFocus();

                // Mostrar el Teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtGruposBuscar, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        //Listener para manerjar la visibilidad del boton de busqueda
        txtGruposBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el edittext y muestra el textview
                textviewGruposBuscar.setVisibility(View.VISIBLE);
                txtGruposBuscar.setVisibility(View.GONE);
                imgBuscar.setVisibility(View.VISIBLE);
                imgBuscar2.setVisibility(View.GONE);
                // Cierra el teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        //Listener para manejar el cierre del teclado con el boton de enter
        txtGruposBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //obtiene el texto de busqueda
                    String query = txtGruposBuscar.getText().toString();

                    //llama el asynctask
                    new BuscarGruposAsyncTask(ActivityGruposBuscar.this, lista, adapter)
                            .execute("62", query); // Reemplazar "1" con el idusuario

                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        imgBuscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtiene el texto de busqueda
                String query = txtGruposBuscar.getText().toString();

                //llama el asynctask
                new BuscarGruposAsyncTask(ActivityGruposBuscar.this, lista, adapter)
                        .execute("56", query); // Reemplazar "1" con el idusuario
            }
        });

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_GruposBuscarAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_GrupoBuscarbotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.txtViewNavGrupos) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.txtviewNavInicio) {
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (view.getId() == R.id.iconNavGrupos) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.iconNavInicio) {
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };


        // Asigna los listeners a los botones de "Atrás"
        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}