package com.example.proyectogrupo1musicstore;

import static com.example.proyectogrupo1musicstore.R.id.menu_editar_perfil;

import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.token;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Adapters.AppData;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Activity_PerfilPersonal extends AppCompatActivity {


    int ID;
    private token acceso = new token(this);
    final int myMenuId = menu_editar_perfil;
    TextView txtNombreCompleto, txtUsername, txtCorreo, txtSeguidores, txtSiguiendo;
    ImageView imgPFP;
    LinearLayout btnAtras;
    LinearLayout verSeguidores, verSeguidos;
    int intervaloActualizacion = 5 * 1000;

    // Crea un handler para manejar la actualización periódica
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Actualiza la información del usuario
            actualizarInformacionUsuario();
            // Programa la próxima ejecución después del intervalo especificado
            handler.postDelayed(this, intervaloActualizacion);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);

        ID = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        txtCorreo = findViewById(R.id.txtCorreoPerfilPersonal);
        txtUsername = findViewById(R.id.txtUsernamePerfilPersonal);
        txtNombreCompleto = findViewById(R.id.txtNombreCompletoPerfilPersonal);
        imgPFP = findViewById(R.id.imgPerfilPersonal);
        txtSeguidores = findViewById(R.id.txtSeguidores);
        txtSiguiendo = findViewById(R.id.txtSiguiendo);
        verSeguidores = findViewById(R.id.layoutVerSeguidores);
        verSeguidos = findViewById(R.id.layoutVerSeguidos);
        btnAtras = findViewById(R.id.btnAtras);

        verSeguidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidores"
                abrirListaSeguidores(ID);
            }
        });

        verSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidos"
                abrirListaSeguidos(ID);
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inicia la actualización periódica
        handler.postDelayed(runnable, intervaloActualizacion);

        ImageView btnMenu = findViewById(R.id.btnMenu);

        // Configuración del menú desplegable
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_editar_perfil, popupMenu.getMenu());


        // Manejar clics en los elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_editar_perfil) {
                    // Abrir la actividad Activity_EditarPerfil
                    Intent intent = new Intent(Activity_PerfilPersonal.this, Activity_EditarPerfil.class);
                    intent.putExtra("IdPersonal", ID);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Mostrar el menú desplegable cuando se hace clic en el botón
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        // Realiza la primera actualización de la información del usuario
        actualizarInformacionUsuario();
    }

    private void actualizarInformacionUsuario() {
        String url = "https://phpclusters-152621-0.cloudclusters.net/mostrarUsuario.php?id=" + ID + "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("usuario");
                            String nombreCompleto = response.getString("nombres") + " " + response.getString("apellidos");
                            String correo = response.getString("correo");
                            String imageUrl = response.getString("enlacefoto");
                            String count_seguidores = response.getString("count_seguidores");
                            String count_seguidos = response.getString("count_seguidos");

                            txtUsername.setText(username);
                            txtNombreCompleto.setText(nombreCompleto);
                            txtCorreo.setText(correo);
                            txtSeguidores.setText(count_seguidores);
                            txtSiguiendo.setText(count_seguidos);

                            try {
                                // Parsear la URL
                                URL url = new URL(imageUrl);

                                // Obtener el protocolo y el host
                                String protocol = url.getProtocol();
                                String host = url.getHost();

                                // Verificar si la URL comienza con la cadena específica
                                String prefijo = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil/";
                                if (imageUrl.startsWith(prefijo)) {
                                    // Obtener la ruta completa y el query
                                    String pathAndQuery = url.getPath() + "?" + url.getQuery();

                                    // Decodificar la ruta y el query
                                    String decodedPathAndQuery = URLDecoder.decode(pathAndQuery, "UTF-8");

                                    // Dividir la URL en dos partes
                                    String primeraParte = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil%2F";
                                    String segundaParte = url.getFile().substring(url.getPath().lastIndexOf('/') + 1);

                                    imageUrl = primeraParte + segundaParte;
                                    Log.d("Estado URL", "url incorrecta: "+imageUrl);
                                } else {
                                    Log.d("Estado URL", "url correcta: "+imageUrl);
                                    // La URL no comienza con la cadena específica, no hacer nada o manejar según sea necesario
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Glide.with(Activity_PerfilPersonal.this)
                                    .load(imageUrl)
                                    .into(imgPFP);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void abrirListaSeguidores(int id) {
        Intent intent = new Intent(Activity_PerfilPersonal.this, ActivityListaSeguidores.class);
        AppData.getInstance().setId(String.valueOf(ID));
        startActivity(intent);
    }

    private void abrirListaSeguidos(int id) {
        Intent intent = new Intent(Activity_PerfilPersonal.this, ActivityListaSeguidos.class);
        AppData.getInstance().setId(String.valueOf(ID));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene la actualización periódica
        handler.removeCallbacks(runnable);
    }
}