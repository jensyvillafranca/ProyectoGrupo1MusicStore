package com.example.proyectogrupo1musicstore.Activities.Perfil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.Adapters.AppData;
import com.example.proyectogrupo1musicstore.Models.User;
import com.example.proyectogrupo1musicstore.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ActivityListaSeguidores extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> listaDeUsuarios;
    ImageButton btnUsuariosBuscarAtras;

    ImageView imageViewUsuarioBuscar, imageViewUsuarioBuscar2;
    EditText editTextUsuarioBuscar;

    TextView txtUsuarioBuscarBuscarUsuario;
    String seguidoseguir = "false";
    int IdPersonal = Integer.parseInt(AppData.getInstance().getId());
    int IdUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_seguidores);
        recyclerView = findViewById(R.id.recyclerview_UsuarioBuscar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnUsuariosBuscarAtras = findViewById(R.id.btn_UsuariosBuscarAtras);
        imageViewUsuarioBuscar = (ImageView) findViewById(R.id.imageViewUsuarioBuscar);
        imageViewUsuarioBuscar2 = (ImageView) findViewById(R.id.imageViewUsuarioBuscar2);
        txtUsuarioBuscarBuscarUsuario = (TextView) findViewById(R.id.txtUsuarioBuscarBuscarUsuario);
        editTextUsuarioBuscar = (EditText) findViewById(R.id.editTextUsuarioBuscar);
        IdUsuario = getIntent().getIntExtra("IdUsuario", -1);
        listaDeUsuarios = new ArrayList<>();

        btnUsuariosBuscarAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para ejecutar al hacer clic en el botón
                finish();
            }
        });

        txtUsuarioBuscarBuscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el textview y muestra el edittext
                txtUsuarioBuscarBuscarUsuario.setVisibility(View.GONE);
                editTextUsuarioBuscar.setVisibility(View.VISIBLE);
                imageViewUsuarioBuscar.setVisibility(View.GONE);
                imageViewUsuarioBuscar2.setVisibility(View.VISIBLE);
                editTextUsuarioBuscar.requestFocus();

                // Mostrar el Teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextUsuarioBuscar, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        editTextUsuarioBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde el textview y muestra el edittext
                txtUsuarioBuscarBuscarUsuario.setVisibility(View.VISIBLE);
                editTextUsuarioBuscar.setVisibility(View.GONE);
                imageViewUsuarioBuscar.setVisibility(View.VISIBLE);
                imageViewUsuarioBuscar2.setVisibility(View.GONE);
                // Mostrar el Teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextUsuarioBuscar, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        editTextUsuarioBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //obtiene el texto de busqueda
                    String query = editTextUsuarioBuscar.getText().toString();
                    listaDeUsuarios.clear();

                    String url = "https://phpclusters-156700-0.cloudclusters.net/buscarUsuarios.php?id=" + IdPersonal + "&buscar=" + query;
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject usuarioJson = response.getJSONObject(i);

                                            int idusuario = usuarioJson.getInt("idusuario");
                                            String nombres = usuarioJson.getString("nombres");
                                            String apellidos = usuarioJson.getString("apellidos");
                                            String correo = usuarioJson.getString("correo");
                                            String usuario = usuarioJson.getString("usuario");
                                            String enlacefoto = usuarioJson.getString("enlacefoto");

                                            try {
                                                // Parsear la URL
                                                URL url = new URL(enlacefoto);

                                                // Obtener el protocolo y el host
                                                String protocol = url.getProtocol();
                                                String host = url.getHost();

                                                // Verificar si la URL comienza con la cadena específica
                                                String prefijo = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil/";
                                                if (enlacefoto.startsWith(prefijo)) {
                                                    // Obtener la ruta completa y el query
                                                    String pathAndQuery = url.getPath() + "?" + url.getQuery();

                                                    // Decodificar la ruta y el query
                                                    String decodedPathAndQuery = URLDecoder.decode(pathAndQuery, "UTF-8");

                                                    // Dividir la URL en dos partes
                                                    String primeraParte = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil%2F";
                                                    String segundaParte = url.getFile().substring(url.getPath().lastIndexOf('/') + 1);

                                                    enlacefoto = primeraParte + segundaParte;
                                                } else {
                                                    // La URL no comienza con la cadena específica, no hacer nada o manejar según sea necesario
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            int idVisualizacion = usuarioJson.getInt("idvisualizacion");
                                            String seguirseguido = usuarioJson.getString("sigue");
                                            User user = new User(idusuario, nombres, apellidos, correo, usuario, enlacefoto, seguirseguido, idVisualizacion);
                                            listaDeUsuarios.add(user);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    ConfigurarRecyclerView();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Manejar errores de la solicitud
                                }
                            });

                    Volley.newRequestQueue(ActivityListaSeguidores.this).add(jsonArrayRequest);

                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });




















//----------------------------------------------------------------------------------------------------------------------------

        if(IdUsuario == -1){
            String url = "https://phpclusters-156700-0.cloudclusters.net/mostrarUsuariosSeguidor.php?id="+IdPersonal+"";
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject usuarioJson = response.getJSONObject(i);

                                    int idusuario = usuarioJson.getInt("idusuario");
                                    String nombres = usuarioJson.getString("nombres");
                                    String apellidos = usuarioJson.getString("apellidos");
                                    String correo = usuarioJson.getString("correo");
                                    String usuario = usuarioJson.getString("usuario");
                                    String enlacefoto = usuarioJson.getString("enlacefoto");
                                    try {
                                        // Parsear la URL
                                        URL url = new URL(enlacefoto);

                                        // Obtener el protocolo y el host
                                        String protocol = url.getProtocol();
                                        String host = url.getHost();

                                        // Verificar si la URL comienza con la cadena específica
                                        String prefijo = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil/";
                                        if (enlacefoto.startsWith(prefijo)) {
                                            // Obtener la ruta completa y el query
                                            String pathAndQuery = url.getPath() + "?" + url.getQuery();

                                            // Decodificar la ruta y el query
                                            String decodedPathAndQuery = URLDecoder.decode(pathAndQuery, "UTF-8");

                                            // Dividir la URL en dos partes
                                            String primeraParte = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil%2F";
                                            String segundaParte = url.getFile().substring(url.getPath().lastIndexOf('/') + 1);

                                            enlacefoto = primeraParte + segundaParte;
                                            Log.d("Estado URL", "url incorrecta: "+enlacefoto);
                                        } else {

                                            Log.d("Estado URL", "url CORRECTAAA: "+enlacefoto);
                                            // La URL no comienza con la cadena específica, no hacer nada o manejar según sea necesario
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    int idVisualizacion = usuarioJson.getInt("idvisualizacion");
                                    String seguirseguido = usuarioJson.getString("sigue");
                                    if(IdPersonal == idusuario){
                                        seguirseguido = "usuario";
                                    }
                                    User user = new User(idusuario, nombres, apellidos, correo, usuario, enlacefoto, seguirseguido, idVisualizacion);
                                    listaDeUsuarios.add(user);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            ConfigurarRecyclerView();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar errores de la solicitud
                        }
                    });

            Volley.newRequestQueue(this).add(jsonArrayRequest);
        }else {
            String url = "https://phpclusters-156700-0.cloudclusters.net/mostrarUsuariosSeguidor.php?id="+IdUsuario+"";
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject usuarioJson = response.getJSONObject(i);

                                    int idusuario = usuarioJson.getInt("idusuario");
                                    String nombres = usuarioJson.getString("nombres");
                                    String apellidos = usuarioJson.getString("apellidos");
                                    String correo = usuarioJson.getString("correo");
                                    String usuario = usuarioJson.getString("usuario");
                                    String enlacefoto = usuarioJson.getString("enlacefoto");

                                    try {
                                        // Parsear la URL
                                        URL url = new URL(enlacefoto);

                                        // Obtener el protocolo y el host
                                        String protocol = url.getProtocol();
                                        String host = url.getHost();

                                        // Verificar si la URL comienza con la cadena específica
                                        String prefijo = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil/";
                                        if (enlacefoto.startsWith(prefijo)) {
                                            // Obtener la ruta completa y el query
                                            String pathAndQuery = url.getPath() + "?" + url.getQuery();

                                            // Decodificar la ruta y el query
                                            String decodedPathAndQuery = URLDecoder.decode(pathAndQuery, "UTF-8");

                                            // Dividir la URL en dos partes
                                            String primeraParte = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil%2F";
                                            String segundaParte = url.getFile().substring(url.getPath().lastIndexOf('/') + 1);

                                            enlacefoto = primeraParte + segundaParte;
                                            Log.d("Estado URL", "url incorrecta: "+enlacefoto);
                                        } else {

                                            Log.d("Estado URL", "url CORRECTAAA: "+enlacefoto);
                                            // La URL no comienza con la cadena específica, no hacer nada o manejar según sea necesario
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    int idVisualizacion = usuarioJson.getInt("idvisualizacion");
                                    String seguirseguido = usuarioJson.getString("sigue");
                                    if(IdPersonal == idusuario){
                                        seguirseguido = "usuario";
                                    }
                                    User user = new User(idusuario, nombres, apellidos, correo, usuario, enlacefoto, seguirseguido, idVisualizacion);
                                    listaDeUsuarios.add(user);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            ConfigurarRecyclerView();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar errores de la solicitud
                        }
                    });

            Volley.newRequestQueue(this).add(jsonArrayRequest);
        }

    }

    private void ConfigurarRecyclerView() {
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itemseguidores, parent, false);
                return new UsuarioViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                User usuario = listaDeUsuarios.get(position);

                if (holder instanceof ActivityListaSeguidores.UsuarioViewHolder) {
                    ActivityListaSeguidores.UsuarioViewHolder usuarioViewHolder = (ActivityListaSeguidores.UsuarioViewHolder) holder;
                    usuarioViewHolder.nombreTextView.setText(usuario.getUsuario());
                    Glide.with(ActivityListaSeguidores.this).load(usuario.getEnlacefoto()).into(usuarioViewHolder.imgPFP);
                    int idUsuarioSeguidor = usuario.getIdusuario();
                    String seguirseguido = usuario.getSeguirseguido();
                    if("true".equals(seguirseguido)){
                        usuarioViewHolder.btnSeguirSeguido.setText(R.string.seguido);
                        usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                        usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                    }else if ("false".equals(seguirseguido)){
                        usuarioViewHolder.btnSeguirSeguido.setText(R.string.seguir);
                        usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                        usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                    }else if ("solicitado".equals(seguirseguido)){
                        seguidoseguir = "false";
                        usuarioViewHolder.btnSeguirSeguido.setText(R.string.pendiente);
                        usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                        usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                    } else if ("usuario".equals(seguirseguido)) {
                        usuarioViewHolder.btnSeguirSeguido.setVisibility(View.GONE);
                    }

                    usuarioViewHolder.btnSeguirSeguido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("VerificarClick", "Si se pudo");
                            if(usuario.getIdVisualizacion() == 0){
                                if(seguidoseguir.equals("true")) {
                                    Log.d("VerificarTrue", "Si se pudo");
                                    seguidoseguir = "false";
                                    eliminarSeguidor(idUsuarioSeguidor);
                                    usuarioViewHolder.btnSeguirSeguido.setText(R.string.seguir);
                                    usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                                    usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                                } else if (seguidoseguir.equals("false")) {
                                    Log.d("VerificarFalse", "Si se pudo");
                                    seguidoseguir = "true";
                                    crearSeguidor(idUsuarioSeguidor);
                                    usuarioViewHolder.btnSeguirSeguido.setText(R.string.pendiente);
                                    usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                    usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                }else if ("usuario".equals(seguirseguido)) {
                                }
                            }

                            if(usuario.getIdVisualizacion() == 1){
                                if(seguidoseguir.equals("true")) {
                                    seguidoseguir = "false";
                                    eliminarSeguidor(idUsuarioSeguidor);
                                    usuarioViewHolder.btnSeguirSeguido.setText(R.string.seguir);
                                    usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.whiteseguir));
                                    usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguir);
                                } else if (seguidoseguir.equals("false")) {
                                    seguidoseguir = "true";
                                    crearSeguidor(idUsuarioSeguidor);
                                    usuarioViewHolder.btnSeguirSeguido.setText(R.string.seguido);
                                    usuarioViewHolder.btnSeguirSeguido.setTextColor(getResources().getColor(R.color.azulseguido));
                                    usuarioViewHolder.btnSeguirSeguido.setBackgroundResource(R.drawable.botonseguido);
                                }else if ("usuario".equals(seguirseguido)) {
                                }
                            }
                        }
                    });
                    
                    if(usuario.getIdusuario() == IdPersonal){
                        usuarioViewHolder.verUsuario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                abrirActivityPerfilPersonal();
                            }
                        });
                    }else {
                        usuarioViewHolder.verUsuario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                abrirActivityPerfilUsuario(usuario.getIdusuario());
                            }
                        });
                    }


                }
            }

            @Override
            public int getItemCount() {
                return listaDeUsuarios.size();
            }
        };

        recyclerView.setAdapter(adapter);
    }

    private void eliminarSeguidor(int idUsuarioSeguido) {
        String urlEliminarSeguido = "https://phpclusters-156700-0.cloudclusters.net/eliminarSeguidor.php?idUsuarioSeguido=" + idUsuarioSeguido +"&idUsuario="+IdPersonal+"";
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


    private void crearSeguidor(int idUsuarioSeguidor) {
        String urlCrearSeguidor = "https://phpclusters-156700-0.cloudclusters.net/crearSeguidor.php?idUsuarioSeguido=" + idUsuarioSeguidor +"&idUsuario="+IdPersonal+"";
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



    // ViewHolder para representar los elementos de la vista del RecyclerView
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        Button btnSeguirSeguido;
        ImageView imgPFP;
        LinearLayout verUsuario;
        public UsuarioViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.txtListItemNombreUsuario);
            btnSeguirSeguido = itemView.findViewById(R.id.btnSeguidoSeguir);
            imgPFP = itemView.findViewById(R.id.imageviewListItemImageUsuario);
            verUsuario = itemView.findViewById(R.id.layoutVerUsuario);
        }
    }

    private void abrirActivityPerfilPersonal() {
        Intent intent = new Intent(this, Activity_PerfilPersonal.class);
        startActivity(intent);
    }

    private void abrirActivityPerfilUsuario(int id) {
        Intent intent = new Intent(this, Activity_PerfilUsuario.class);
        intent.putExtra("IdUsuario", id);
        startActivity(intent);
    }
}
