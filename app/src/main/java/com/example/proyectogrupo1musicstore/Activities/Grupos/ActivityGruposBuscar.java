package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.app.ProgressDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterBuscarGrupos;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.BuscarGruposAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationGruposPrincipalClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.ArrayList;
import java.util.List;

public class ActivityGruposBuscar extends AppCompatActivity {

    // Declaración de variables
    RecyclerView lista;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, textviewGruposBuscar;
    EditText txtGruposBuscar;
    ImageView imgBuscar, imgBuscar2;
    ProgressDialog progressDialog;
    private NavigationClickListener navigationClickListener;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos_buscar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        // Inicializa listener para elementos
        navigationClickListener = new NavigationClickListener(this, this);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

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



        // Creación de una lista de elementos de vistaDeGrupo
        List<buscarGrupo> dataList = new ArrayList<>();

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        CustomAdapterBuscarGrupos adapter = new CustomAdapterBuscarGrupos(this, dataList);
        lista.setAdapter(adapter);

        try{
            String busqueda;
            busqueda = getIntent().getStringExtra("busqueda");
            if(!busqueda.isEmpty()){
                txtGruposBuscar.setText(busqueda);
                String query = txtGruposBuscar.getText().toString();
                new BuscarGruposAsyncTask(ActivityGruposBuscar.this, lista, adapter)
                        .execute(String.valueOf(idUsuario), query);
            }
        }catch (Exception e){
            Log.e("Error Parameter", "No se encontro un parametro");
        }

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
                            .execute(String.valueOf(idUsuario), query);

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
                        .execute(String.valueOf(idUsuario), query);
            }
        });

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textviewAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}