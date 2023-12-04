package com.example.proyectogrupo1musicstore.Activities.Buscar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterBuscarUsuarios;
import com.example.proyectogrupo1musicstore.Models.buscarUsuario;
import com.example.proyectogrupo1musicstore.NetworkTasks.BuscarUsuarios.BuscarUsuariosAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.util.ArrayList;
import java.util.List;

public class ActivityBuscarPrincipal extends AppCompatActivity {

    // Declaración de variables
    RecyclerView lista;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, textviewBuscar;
    EditText txtBuscar;
    ImageView imgBuscar, imgBuscar2;
    ProgressDialog progressDialog;

    private NavigationClickListener navigationClickListener;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_principal);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        // Inicializa listener para elementos
        navigationClickListener = new NavigationClickListener(this, this);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        // Inicialización de vistas y elementos del diseño
        lista = findViewById(R.id.recyclerview_BuscarPrincipal);
        drawerLayout = findViewById(R.id.drawer_layoutBuscarPrincipal);
        openMenuButton = findViewById(R.id.btn_BuscarPrincipalMenu);
        botonAtras = findViewById(R.id.btn_BuscarPrincipalAtras);
        textviewAtras = findViewById(R.id.textview_BuscarPrincipalbotAtras);
        textviewBuscar = findViewById(R.id.txtBuscarPrincipalBuscarGrupo);
        txtBuscar = findViewById(R.id.editTextBuscarPrincipal);
        imgBuscar = findViewById(R.id.imageViewBuscarPrincipal);
        imgBuscar2 = findViewById(R.id.imageViewBuscarPrincipal2);

        // Creación de una lista de elementos
        List<buscarUsuario> dataList = new ArrayList<>();

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        CustomAdapterBuscarUsuarios adapter = new CustomAdapterBuscarUsuarios(this, dataList, idUsuario);
        lista.setAdapter(adapter);

        try{
            String busqueda;
            busqueda = getIntent().getStringExtra("busqueda");
            if(!busqueda.isEmpty()){
                txtBuscar.setText(busqueda);
                String query = txtBuscar.getText().toString();
                new BuscarUsuariosAsyncTask(ActivityBuscarPrincipal.this, lista, adapter)
                        .execute(String.valueOf(idUsuario), query);
            }
        }catch (Exception e){
            Log.e("Error Parameter", "No se encontro un parametro");
        }

        //Listener para manerjar la visibilidad del boton de busqueda
        textviewBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el textview y muestra el edittext
                textviewBuscar.setVisibility(View.GONE);
                txtBuscar.setVisibility(View.VISIBLE);
                imgBuscar.setVisibility(View.GONE);
                imgBuscar2.setVisibility(View.VISIBLE);
                txtBuscar.requestFocus();

                // Mostrar el Teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtBuscar, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        //Listener para manerjar la visibilidad del boton de busqueda
        txtBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el edittext y muestra el textview
                textviewBuscar.setVisibility(View.VISIBLE);
                txtBuscar.setVisibility(View.GONE);
                imgBuscar.setVisibility(View.VISIBLE);
                imgBuscar2.setVisibility(View.GONE);
                // Cierra el teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        //Listener para manejar el cierre del teclado con el boton de enter
        txtBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //obtiene el texto de busqueda
                    String query = txtBuscar.getText().toString();

                    //llama el asynctask
                    new BuscarUsuariosAsyncTask(ActivityBuscarPrincipal.this, lista, adapter)
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
                String query = txtBuscar.getText().toString();

                //llama el asynctask
                new BuscarUsuariosAsyncTask(ActivityBuscarPrincipal.this, lista, adapter)
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