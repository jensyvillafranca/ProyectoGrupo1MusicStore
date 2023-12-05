package com.example.proyectogrupo1musicstore.Activities.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.Adapters.AppData;
import com.example.proyectogrupo1musicstore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;

public class Activity_PerfilUsuario extends AppCompatActivity {

    int IdPersonal = Integer.parseInt(AppData.getInstance().getId());
    int IdUsuario;
    TextView txtNombreCompleto, txtUsername, txtCorreo, txtSeguidores, txtSiguiendo;
    ImageView imgPFP;

    Button btnSeguirSeguido, btnConfirmarSeguir, btnEliminarSeguir;
    FrameLayout perfilPublico, perfilPrivado;
    LinearLayout verSeguidores,btnAtras, verSeguidos, layoutSeguir, layoutConfirmar;
    int intervaloActualizacion = 5 * 1000;
    String seguirseguido;

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
        setContentView(R.layout.activity_perfil_usuario);



        // Obtener IdPersonal de AppData
        IdPersonal = Integer.parseInt(AppData.getInstance().getId());

        // Obtener IdUsuario de los extras de la intención
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("IdUsuario")) {
            IdUsuario = intent.getIntExtra("IdUsuario", -1);
        } else {
            // Manejar el caso en que no se proporciona "IdUsuario" en los extras
            // Puedes establecer un valor predeterminado o mostrar un mensaje de error
            Toast.makeText(this, "Error: IdUsuario no proporcionado", Toast.LENGTH_SHORT).show();
            // Puedes cerrar la actividad si el IdUsuario es crítico para el funcionamiento
            finish();
            return;
        }
        txtCorreo = findViewById(R.id.txtCorreoPerfilPersonal);
        txtUsername = findViewById(R.id.txtUsernamePerfilPersonal);
        txtNombreCompleto = findViewById(R.id.txtNombreCompletoPerfilPersonal);
        IdUsuario = getIntent().getIntExtra("IdUsuario", -1);


        imgPFP = findViewById(R.id.imgPerfilPersonal);
        txtSeguidores = findViewById(R.id.txtSeguidores);
        txtSiguiendo = findViewById(R.id.txtSiguiendo);
        verSeguidores = findViewById(R.id.layoutVerSeguidores);
        verSeguidos = findViewById(R.id.layoutVerSeguidos);
        perfilPrivado = findViewById(R.id.layout_privado);
        layoutSeguir = findViewById(R.id.layout_seguir);
        layoutConfirmar = findViewById(R.id.layout_confirmar);
        perfilPublico = findViewById(R.id.layout_publico);
        btnSeguirSeguido = findViewById(R.id.btnSeguidoSeguir);
        btnConfirmarSeguir = findViewById(R.id.btnConfirmarSeguir);
        btnEliminarSeguir = findViewById(R.id.btnEliminarSeguir);
        btnAtras = findViewById(R.id.btnAtras);

        verSeguidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidores"
                abrirListaSeguidores();
            }
        });

        verSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidos"
                abrirListaSeguidos();
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

        // Realiza la primera actualización de la información del usuario
        actualizarInformacionUsuario();
    }

    private void actualizarInformacionUsuario() {
        String url = "https://phpclusters-156700-0.cloudclusters.net/mostrarUsuarioPerfil.php?id="+IdPersonal+"&idUsuarioSeguido="+IdUsuario+"";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("usuario");
                            String nombreCompleto = response.getString("nombres") + " " + response.getString("apellidos");
                            String correo = response.getString("correo");
                            String imageUrl = response.getString("enlacefoto");

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
                                } else {
                                    // La URL no comienza con la cadena específica, no hacer nada o manejar según sea necesario
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String count_seguidores = response.getString("count_seguidores");
                            String count_seguidos = response.getString("count_seguidos");
                            int idVisualizacion = Integer.parseInt(response.getString("idvisualizacion"));
                            seguirseguido = response.getString("sigue");
                            String solicitud = response.getString("solicitud");

                            if("solicitud".equals(solicitud)){
                                layoutSeguir.setVisibility(View.GONE);
                                layoutConfirmar.setVisibility(View.VISIBLE);
                            }else {
                                layoutSeguir.setVisibility(View.VISIBLE);
                                layoutConfirmar.setVisibility(View.GONE);
                                if ("true".equals(seguirseguido)) {
                                    btnSeguirSeguido.setText(R.string.seguido);
                                    btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                    btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                } else if ("false".equals(seguirseguido)) {
                                    btnSeguirSeguido.setText(R.string.seguir);
                                    btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                                    btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                                } else if ("solicitado".equals(seguirseguido)) {
                                    seguirseguido = "false";
                                    btnSeguirSeguido.setText(R.string.pendiente);
                                    btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                    btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                }
                            }

                            btnConfirmarSeguir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    crearSeguidor(IdUsuario, solicitud);
                                    layoutSeguir.setVisibility(View.VISIBLE);
                                    layoutConfirmar.setVisibility(View.GONE);
                                }
                            });
                            btnEliminarSeguir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    eliminarSeguidor(IdUsuario);
                                    layoutSeguir.setVisibility(View.VISIBLE);
                                    layoutConfirmar.setVisibility(View.GONE);
                                }
                            });
                            btnSeguirSeguido.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    if(idVisualizacion == 0){
                                        if(seguirseguido.equals("true")) {
                                            seguirseguido = "false";
                                            eliminarSeguidor(IdUsuario);
                                            btnSeguirSeguido.setText(R.string.seguir);
                                            btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                                            btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                                        }else if (seguirseguido.equals("false")) {
                                            seguirseguido = "true";
                                            crearSeguidor(IdUsuario, "null");
                                            btnSeguirSeguido.setText(R.string.pendiente);
                                            btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                            btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                        }
                                    }

                                    if(idVisualizacion == 1){
                                        if(seguirseguido.equals("true")) {
                                            seguirseguido = "false";
                                            eliminarSeguidor(IdUsuario);
                                            btnSeguirSeguido.setText(R.string.seguir);
                                            btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                                            btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                                        }else if (seguirseguido.equals("false")) {
                                            seguirseguido = "true";
                                            crearSeguidor(IdUsuario, "null");
                                            btnSeguirSeguido.setText(R.string.seguido);
                                            btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                            btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                        }
                                    }


                                }
                            });


                            txtUsername.setText(username);
                            txtNombreCompleto.setText(nombreCompleto);
                            txtCorreo.setText(correo);
                            txtSeguidores.setText(count_seguidores);
                            txtSiguiendo.setText(count_seguidos);

                            Glide.with(Activity_PerfilUsuario.this)
                                    .load(imageUrl)
                                    .into(imgPFP);

                            if (idVisualizacion == 0) {
                                perfilPublico.setVisibility(View.GONE);
                                perfilPrivado.setVisibility(View.VISIBLE);
                            } else {
                                perfilPublico.setVisibility(View.VISIBLE);
                                perfilPrivado.setVisibility(View.GONE);
                            }

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

    private void abrirListaSeguidores() {
        Intent intent = new Intent(Activity_PerfilUsuario.this, ActivityListaSeguidores.class);
        intent.putExtra("IdUsuario", IdUsuario);
        startActivity(intent);
    }

    private void abrirListaSeguidos() {
        Intent intent = new Intent(Activity_PerfilUsuario.this, ActivityListaSeguidos.class);
        intent.putExtra("IdUsuario", IdUsuario);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detiene la actualización periódica
        handler.removeCallbacks(runnable);
    }

    private void eliminarSeguidor(int idUsuarioSeguido) {
        String urlEliminarSeguido = "https://phpclusters-156700-0.cloudclusters.net/eliminarSeguidor.php?idUsuarioSeguido=" + IdUsuario +"&idUsuario="+IdPersonal+"";
        JSONObject postData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlEliminarSeguido, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void crearSeguidor(int idUsuarioSeguidor, String Info) {
        String urlCrearSeguidor;

        if("solicitud".equals(Info)){
            urlCrearSeguidor = "https://phpclusters-156700-0.cloudclusters.net/confirmarSolicitudSeguidor.php?idUsuarioSeguido=" + IdPersonal +"&idUsuario="+idUsuarioSeguidor;
        }else {
            urlCrearSeguidor = "https://phpclusters-156700-0.cloudclusters.net/crearSeguidor.php?idUsuarioSeguido=" + idUsuarioSeguidor +"&idUsuario="+IdPersonal+"";
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("idUsuarioSeguido", idUsuarioSeguidor);
            postData.put("idUsuario", IdPersonal); // Puedes necesitar enviar el ID del usuario que está realizando la acción
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlCrearSeguidor, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

}
