package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoEditar;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.InformacionGrupoEditarAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditarGrupo extends AppCompatActivity implements InformacionGrupoEditarAsyncTask.DataFetchListener{

    RecyclerView recyclerViewIntegrantes;
    ImageButton imagebuttonAtras;
    ImageView imagebuttonEditarFoto;
    TextView textviewAtras, textviewNombreGrupo;
    EditText txtDescripcion;
    CheckBox checkTipoGrupo;
    Button btnGuardar;
    ProgressDialog  progressDialog;
    private int idgrupo;
    private token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_grupo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        idgrupo = getIntent().getIntExtra("idgrupo", 0);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        imagebuttonAtras = (ImageButton) findViewById(R.id.btn_EditarGrupoAtras);
        imagebuttonEditarFoto = (ImageView) findViewById(R.id.imageview_EditarGrupoSubir2);
        textviewAtras = (TextView) findViewById(R.id.textview_EditarGrupoBotAtras);
        textviewNombreGrupo = (TextView) findViewById(R.id.text_EditarGrupoNombreGrupo);
        txtDescripcion = (EditText) findViewById(R.id.text_EditarGrupoDescripcion);
        checkTipoGrupo = (CheckBox) findViewById(R.id.checkboxEditarGrupo);
        btnGuardar = (Button) findViewById(R.id.btnEditarGrupoUpdate);
        recyclerViewIntegrantes = (RecyclerView) findViewById(R.id.recyclerview_EditarGrupo);

        // Creación de una lista de elementos de integrantesItem
        List<integrantesItem> integrantesList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        IntegrantesAdapter integrantesAdapter = new IntegrantesAdapter(this, integrantesList);
        recyclerViewIntegrantes.setAdapter(integrantesAdapter);

        //Configuracion del administrador de diseño - integrantes
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewIntegrantes.setLayoutManager(layoutManager);

        // Fetch data from the server
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerInformacionGrupoEditar.php";
        progressDialog.show();
        new InformacionGrupoEditarAsyncTask(this).execute(url, String.valueOf(idgrupo));
    }

    @Override
    public void onDataFetched(List<informacionGrupoEditar> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            informacionGrupoEditar groupInfo = dataList.get(0);

            textviewNombreGrupo.setText(groupInfo.getNombre());
            imagebuttonEditarFoto.setImageBitmap(groupInfo.getImage());
            txtDescripcion.setText(groupInfo.getDescripcion());

            if(groupInfo.getVisualizacion()==1){
                checkTipoGrupo.setChecked(true);
            }else{
                checkTipoGrupo.setChecked(false);
            }
        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }
}