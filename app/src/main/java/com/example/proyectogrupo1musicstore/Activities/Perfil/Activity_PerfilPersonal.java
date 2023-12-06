package com.example.proyectogrupo1musicstore.Activities.Perfil;

import com.example.proyectogrupo1musicstore.Adapters.GruposFavoritosAdapter;
import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Models.informacionGruposFavoritos;
import com.example.proyectogrupo1musicstore.Models.informacionPerfil;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.ObtenerPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.InformacionPerfilAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.InsertarSeguidorAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.deleteSeguidorAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.obtenerGruposFavoritosAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.AppData;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;

import java.util.ArrayList;
import java.util.List;

public class Activity_PerfilPersonal extends AppCompatActivity implements InformacionPerfilAsyncTask.DataFetchListener {

    private int idUsuario, idUsuarioVista;
    private token acceso = new token(this);
    private ProgressDialog progressDialog;
    private RecyclerView recyclerViewGruposFavoritos, recycerViewPlaylistFavoritos;
    private DrawerLayout drawerLayout;
    private ImageButton btnMenu;
    private Button btnSeguir;
    TextView txtNombreCompleto, txtUsername, txtCorreo, txtSeguidores, txtSiguiendo, textViewGruposFavorito, textViewPlaylistFavoritos;
    ImageView imgPFP;
    LinearLayout btnAtras;
    LinearLayout verSeguidores, verSeguidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_personal);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        txtCorreo = findViewById(R.id.txtCorreoPerfilPersonal);
        txtUsername = findViewById(R.id.txtUsernamePerfilPersonal);
        txtNombreCompleto = findViewById(R.id.txtNombreCompletoPerfilPersonal);
        imgPFP = findViewById(R.id.imgPerfilPersonal);
        txtSeguidores = findViewById(R.id.txtSeguidoresPerfilPersonal);
        txtSiguiendo = findViewById(R.id.txtSiguiendoPerfilPersonal);
        verSeguidores = findViewById(R.id.layoutVerSeguidoresPerfilPersonal);
        verSeguidos = findViewById(R.id.layoutVerSeguidosPerfilPersonal);
        btnAtras = findViewById(R.id.btnAtras);
        recyclerViewGruposFavoritos = findViewById(R.id.recyclerviewPerfilUsuario);
        recycerViewPlaylistFavoritos = findViewById(R.id.recyclerviewPlaylistPerfil);
        btnMenu = findViewById(R.id.btn_PrincipalDesplegablePerfil);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnSeguir = findViewById(R.id.btnSeguidoSeguir5);
        textViewGruposFavorito = findViewById(R.id.textviewPerfilGruposFavoritos);
        textViewPlaylistFavoritos = findViewById(R.id.textviewPerfilPlaylists);

        try{
            idUsuarioVista = getIntent().getIntExtra("idusuarioVista", 0);
            if(idUsuarioVista==0){
                idUsuarioVista = idUsuario;
            }
        }catch (Exception e){
            Log.e("Error Parameter", "No se encontro un parametro");
        }

        verSeguidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidores"
                abrirListaSeguidores(idUsuario);
            }
        });

        verSeguidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en "Ver Seguidos"
                abrirListaSeguidos(idUsuario);
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Listener para abrir menu de navegacion
        btnMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });

        //Listener para menu de navegacion
        View.OnClickListener buttonClickNav = new NavigationClickListener(this,this);

        // Creación de listas de elementos
        List<informacionGruposFavoritos> gruposList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        GruposFavoritosAdapter gruposAdapter = new GruposFavoritosAdapter(this, gruposList);
        recyclerViewGruposFavoritos.setAdapter(gruposAdapter);

        //Configuracion del administrador de diseño
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewGruposFavoritos.setLayoutManager(layoutManager);
        //Configuracion del administrador de diseño - platlist
        LinearLayoutManager layoutManagerPlaylist = new LinearLayoutManager(this);
        layoutManagerPlaylist.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycerViewPlaylistFavoritos.setLayoutManager(layoutManagerPlaylist);


        //Obtiene informcaion de asynctask
        String url = "https://phpclusters-156700-0.cloudclusters.net/obtenerInformacionPerfil.php";
        progressDialog.show();
        new InformacionPerfilAsyncTask(this).execute(url, String.valueOf(idUsuarioVista), String.valueOf(idUsuario));
        new obtenerGruposFavoritosAsyncTask(Activity_PerfilPersonal.this, gruposAdapter)
                .execute(String.valueOf(idUsuarioVista), String.valueOf(idUsuario), String.valueOf(1));
    }

    @Override
    public void onDataFetched(List<informacionPerfil> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            informacionPerfil perfilInfo = dataList.get(0);

            if(perfilInfo.getIdusuario() == idUsuario){
                List<PlayListItem> playlistitemList = new ArrayList<>();
                // Crea y vincula el adaptador - playadapter
                PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
                recycerViewPlaylistFavoritos.setAdapter(playAdapter);
                new ObtenerPlayListAsyncTask(Activity_PerfilPersonal.this, playAdapter, progressDialog).execute(String.valueOf(idUsuario));
            }else{
                List<PlayListItem> playlistitemList = new ArrayList<>();
                // Crea y vincula el adaptador - playadapter
                PlayListAdapter playAdapter = new PlayListAdapter(this, playlistitemList);
                recycerViewPlaylistFavoritos.setAdapter(playAdapter);
                new ObtenerPlayListAsyncTask(Activity_PerfilPersonal.this, playAdapter, progressDialog).execute(String.valueOf(perfilInfo.getIdusuario()));
            }

            if(perfilInfo.getVisualizacion() == 0 && perfilInfo.getIdusuario()!=idUsuario){
                recycerViewPlaylistFavoritos.setVisibility(View.GONE);
                recyclerViewGruposFavoritos.setVisibility(View.GONE);
                textViewPlaylistFavoritos.setVisibility(View.GONE);
                textViewGruposFavorito.setVisibility(View.GONE);
            }

            txtNombreCompleto.setText(perfilInfo.getNombre());
            txtUsername.setText("@" + perfilInfo.getUsuario());
            txtCorreo.setText(perfilInfo.getCorreo());
            txtSeguidores.setText(String.valueOf(perfilInfo.getNumeroSeguidores()));
            txtSiguiendo.setText(String.valueOf(perfilInfo.getNumeroSeguidos()));
            imgPFP.setImageBitmap(perfilInfo.getFoto());
            if(perfilInfo.getIdusuario()==idUsuario){
                btnSeguir.setVisibility(View.GONE);
            } else if (perfilInfo.getSeguidor()==1)
            {
                btnSeguir.setVisibility(View.VISIBLE);
                btnSeguir.setText("Dejar de Segir");
                btnSeguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new deleteSeguidorAsyncTask(Activity_PerfilPersonal.this).execute(String.valueOf(idUsuarioVista), String.valueOf(idUsuario));
                    }
                });

            } else{
                btnSeguir.setVisibility(View.VISIBLE);
                btnSeguir.setText("Seguir");
                btnSeguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new InsertarSeguidorAsyncTask(Activity_PerfilPersonal.this).execute(String.valueOf(idUsuarioVista), String.valueOf(idUsuario));
                    }
                });
            }
        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    private void abrirListaSeguidores(int id) {
        Intent intent = new Intent(Activity_PerfilPersonal.this, ActivityListaSeguidores.class);
        AppData.getInstance().setId(String.valueOf(idUsuario));
        startActivity(intent);
    }

    private void abrirListaSeguidos(int id) {
        Intent intent = new Intent(Activity_PerfilPersonal.this, ActivityListaSeguidos.class);
        AppData.getInstance().setId(String.valueOf(idUsuario));
        startActivity(intent);
    }
}