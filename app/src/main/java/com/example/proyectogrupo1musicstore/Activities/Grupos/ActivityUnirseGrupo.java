package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.content.DialogInterface;
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

import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.InsertarIntegranteAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ActivityUnirseGrupo extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton openMenuButton, botonAtras;
    TextView textviewAtras, textviewNombreGrupo, textviewIntegrantes, textviewCreadoPor, textviewDescripcion, textviewTipoGrupo;
    Button btnUnirse;
    private List<buscarGrupo> receivedDataList;
    private buscarGrupo object;
    private String tipo;
    ImageView imageGrupo;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_grupo);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");

        // Listener para menu de navegacion
        View.OnClickListener buttonClickNav = new NavigationClickListener(this,this);

        try {
            JsonElement element = JsonParser.parseString(jsonString);

            if (element.isJsonObject()) {
                // Handle object
                object = new Gson().fromJson(element, buscarGrupo.class);
            } else if (element.isJsonArray()) {
                // Handle array
                Type listType = new TypeToken<List<buscarGrupo>>() {}.getType();
                receivedDataList = new Gson().fromJson(element, listType);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        // Inicialización de vistas y elementos del diseño
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGruposUnirse);
        openMenuButton = (ImageButton) findViewById(R.id.btn_GruposUnirseMenu);
        botonAtras = (ImageButton) findViewById(R.id.btn_GruposUnirseAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_GrupoUnirsebotAtras);
        textviewNombreGrupo = (TextView) findViewById(R.id.textviewUnirseNombreGrupo);
        textviewIntegrantes = (TextView) findViewById(R.id.textviewUnirseIntegrantes);
        textviewCreadoPor = (TextView) findViewById(R.id.textviewUnirseCreadoPor);
        textviewDescripcion = (TextView) findViewById(R.id.textviewUnirseDescripcion);
        textviewTipoGrupo = (TextView) findViewById(R.id.textviewUnirseTipoGrupo);
        btnUnirse = (Button) findViewById(R.id.btnUnirse);
        imageGrupo = (ImageView) findViewById(R.id.imageviewUnirseImagenGrupo);

        if(object.getIdvisualizacion()==1){
            tipo = "Grupo Privado";
        }else{
            tipo = "Grupo Publico";
        }

        //coloca calores en los textviews
        imageGrupo.setImageBitmap(object.getImage());
        textviewNombreGrupo.setText(object.getNombre());
        textviewIntegrantes.setText("Integrantes: " + String.valueOf(object.getTotalusuarios()) );
        textviewCreadoPor.setText("Creado Por: " + object.getUsuario());
        textviewDescripcion.setText(object.getDescripcion());
        textviewTipoGrupo.setText(tipo);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        // Listener para el boton unirse
        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(object.getIdvisualizacion()==1){
                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                            .execute(String.valueOf(object.getIdgrupo()), String.valueOf(idUsuario), "Privado", String.valueOf(object.getIdOwner()));
                }else{
                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                            .execute(String.valueOf(object.getIdgrupo()), String.valueOf(idUsuario), "Publico", String.valueOf(object.getIdOwner()));
                }
            }
        });

        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                ConfirmationDialog.showConfirmationDialog(
                        ActivityUnirseGrupo.this, // Replace with your activity or fragment context
                        "Confirmación",
                        "¿Está seguro de que desea unirse al grupo?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario hace click en si
                                if(object.getIdvisualizacion()==1){
                                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                                            .execute(String.valueOf(object.getIdgrupo()), String.valueOf(idUsuario), "Privado", String.valueOf(object.getIdOwner()));
                                }else{
                                    new InsertarIntegranteAsyncTask(ActivityUnirseGrupo.this)
                                            .execute(String.valueOf(object.getIdgrupo()), String.valueOf(idUsuario), "Publico", String.valueOf(object.getIdOwner()));
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario hace click en no
                                dialog.dismiss();
                            }
                        }
                );
            }
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