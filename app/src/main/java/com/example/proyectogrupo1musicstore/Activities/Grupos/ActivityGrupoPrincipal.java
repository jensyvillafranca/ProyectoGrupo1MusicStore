package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapter;
import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.FetchDataAsyncGruposPrincipal;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;

import java.util.List;

public class ActivityGrupoPrincipal extends AppCompatActivity implements FetchDataAsyncGruposPrincipal.DataFetchListener {

    // Declaración de variables
    RecyclerView lista;
    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, txtGruposBuscar, txtNuevoGrupo, txtActualizar, Grupos, Inicio;
    ImageView imageviewGruposBuscar, imageviewNuevoGrupo, imageviewActualizar, iconGrupos, iconInicio;
    CardView buscar, nuevoGrupo, actualizar;
    ProgressDialog progressDialog;
    private com.example.proyectogrupo1musicstore.Utilidades.token token = new token(this);
    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_principal);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_GruposPrincipal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposPrincipalMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposPrincipalAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoPrincipalbotAtras);
        txtGruposBuscar = (TextView) findViewById(R.id.txtGruposPrincipalBuscarGrupo);
        txtNuevoGrupo = (TextView) findViewById(R.id.txtGruposPrincipalNuevoGrupo);
        txtActualizar = (TextView) findViewById(R.id.txtGruposPrincipalActualizar);
        imageviewGruposBuscar = (ImageView) findViewById(R.id.imageviewGruposPrincipalBuscar);
        imageviewNuevoGrupo = (ImageView) findViewById(R.id.imageviewGruposPrincipalNuevoGrupo);
        imageviewActualizar = (ImageView) findViewById(R.id.imageviewGruposPrincipalActualizar);
        buscar = (CardView) findViewById(R.id.cardViewGruposPrincipalBuscar);
        nuevoGrupo = (CardView) findViewById(R.id.cardViewGruposPrincipalNuevo);
        actualizar = (CardView) findViewById(R.id.cardViewGruposPrincipalActualizar);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);

        // Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/principalGrupos.php";
        // Reemplazar por el idusuario - Motivos de prueba
        progressDialog.show();
        new FetchDataAsyncGruposPrincipal(this, progressDialog).execute(url, String.valueOf(idUsuario));


        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        lista.setLayoutManager(layoutManager);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId()==R.id.btn_GruposPrincipalAtras) {
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (view.getId()==R.id.textview_GrupoPrincipalbotAtras){
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (view.getId() == R.id.txtGruposPrincipalBuscarGrupo){
                    actividad = ActivityGruposBuscar.class;
                }
                if (view.getId() == R.id.imageviewGruposPrincipalBuscar){
                    actividad = ActivityGruposBuscar.class;
                }
                if (view.getId() == R.id.cardViewGruposPrincipalBuscar){
                    actividad = ActivityGruposBuscar.class;
                }
                if (view.getId() == R.id.txtGruposPrincipalNuevoGrupo){
                    actividad = ActivityNuevoGrupoIntegrantes.class;
                }
                if (view.getId() == R.id.imageviewGruposPrincipalNuevoGrupo){
                    actividad = ActivityNuevoGrupoIntegrantes.class;
                }
                if (view.getId() == R.id.cardViewGruposPrincipalNuevo){
                    actividad = ActivityNuevoGrupoIntegrantes.class;
                }
                if (view.getId() == R.id.txtGruposPrincipalActualizar){
                    recreate();
                }
                if (view.getId() == R.id.imageviewGruposPrincipalActualizar){
                    recreate();
                }
                if (view.getId() == R.id.cardViewGruposPrincipalActualizar){
                    recreate();
                }
                if (view.getId() == R.id.txtViewNavGrupos) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.txtviewNavInicio) {
                    actividad = ActivityPantallaPrincipal.class;
                }
                if (view.getId() == R.id.iconNavGrupos){
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.iconNavInicio){
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
        txtGruposBuscar.setOnClickListener(buttonClick);
        txtActualizar.setOnClickListener(buttonClick);
        imageviewGruposBuscar.setOnClickListener(buttonClick);
        imageviewActualizar.setOnClickListener(buttonClick);
        buscar.setOnClickListener(buttonClick);
        Grupos.setOnClickListener(buttonClick);
        Inicio.setOnClickListener(buttonClick);
        actualizar.setOnClickListener(buttonClick);
        iconGrupos.setOnClickListener(buttonClick);
        iconInicio.setOnClickListener(buttonClick);
        txtGruposBuscar.setOnClickListener(buttonClick);
        imageviewGruposBuscar.setOnClickListener(buttonClick);
        nuevoGrupo.setOnClickListener(buttonClick);
    }

    @Override
    public void onDataFetched(List<vistaDeGrupo> dataList) {
        // Muestra el Recycle view con la nueva informacion
        progressDialog.dismiss(); // Esconde el spinner de carga
        CustomAdapter adapter = new CustomAdapter(this, dataList, idUsuario, lista);
        lista.setAdapter(adapter);
    }

    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }
}