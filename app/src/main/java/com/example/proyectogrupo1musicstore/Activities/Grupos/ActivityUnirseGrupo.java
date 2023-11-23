package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;

import java.lang.reflect.Type;
import java.util.List;

import com.example.proyectogrupo1musicstore.NetworkTasks.InsertarIntegranteAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ActivityUnirseGrupo extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, Grupos, Inicio, textviewNombreGrupo, textviewIntegrantes, textviewCreadoPor, textviewDescripcion, textviewTipoGrupo;
    Button btnUnirse;
    private List<buscarGrupo> receivedDataList;
    private String tipo;
    ImageView iconGrupos, iconInicio, imageGrupo;
    private com.example.proyectogrupo1musicstore.Utilidades.token token = new token(this);
    private int idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_grupo);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");
        Type listType = new TypeToken<List<buscarGrupo>>() {}.getType();
        receivedDataList = new Gson().fromJson(jsonString, listType);

        // Inicialización de vistas y elementos del diseño
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGruposUnirse);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposUnirseMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposUnirseAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoUnirsebotAtras);
        Grupos = (TextView) findViewById(R.id.txtViewNavGrupos);
        Inicio = (TextView) findViewById(R.id.txtviewNavInicio);
        iconGrupos = (ImageView) findViewById(R.id.iconNavGrupos);
        iconInicio = (ImageView) findViewById(R.id.iconNavInicio);
        textviewNombreGrupo = (TextView) findViewById(R.id.textviewUnirseNombreGrupo);
        textviewIntegrantes = (TextView) findViewById(R.id.textviewUnirseIntegrantes);
        textviewCreadoPor = (TextView) findViewById(R.id.textviewUnirseCreadoPor);
        textviewDescripcion = (TextView) findViewById(R.id.textviewUnirseDescripcion);
        textviewTipoGrupo = (TextView) findViewById(R.id.textviewUnirseTipoGrupo);
        btnUnirse = (Button) findViewById(R.id.btnUnirse);
        imageGrupo = (ImageView) findViewById(R.id.imageviewUnirseImagenGrupo);

        if(receivedDataList.get(0).getIdvisualizacion()==1){
            tipo = "Grupo Privado";
        }else{
            tipo = "Grupo Publico";
        }

        //coloca calores en los textviews
        imageGrupo.setImageBitmap(receivedDataList.get(0).getImage());
        textviewNombreGrupo.setText(receivedDataList.get(0).getNombre());
        textviewIntegrantes.setText("Integrantes: " + String.valueOf(receivedDataList.get(0).getTotalusuarios()) );
        textviewCreadoPor.setText("Creado Por: " + receivedDataList.get(0).getUsuario());
        textviewDescripcion.setText(receivedDataList.get(0).getDescripcion());
        textviewTipoGrupo.setText(tipo);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para el boton unirse
        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receivedDataList.get(0).getIdvisualizacion()==1){
                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                            .execute(String.valueOf(receivedDataList.get(0).getIdgrupo()), String.valueOf(idUsuario), "Privado", String.valueOf(receivedDataList.get(0).getIdOwner()));
                }else{
                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                            .execute(String.valueOf(receivedDataList.get(0).getIdgrupo()), String.valueOf(idUsuario), "Publico", String.valueOf(receivedDataList.get(0).getIdOwner()));
                }
            }
        });

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_GruposUnirseAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_GrupoUnirsebotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
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