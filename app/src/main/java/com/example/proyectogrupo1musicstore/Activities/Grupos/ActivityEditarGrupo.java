package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoEditar;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.FetchMemberDetailsEditarAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.InformacionGrupoEditarAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.UpdateGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityEditarGrupo extends AppCompatActivity implements InformacionGrupoEditarAsyncTask.DataFetchListener {

    RecyclerView recyclerViewIntegrantes;
    ImageButton imagebuttonAtras;
    ImageView imagebuttonEditarFoto;
    TextView textviewAtras, textviewNombreGrupo;
    EditText txtDescripcion;
    CheckBox checkTipoGrupo;
    Button btnGuardar;
    ProgressDialog progressDialog;
    private int idgrupo;
    private token token = new token(this);
    private int idUsuario;
    private int visualizacion;
    private String urlAnterior;
    byte[] imgPerfilByteArray;
    private informacionGrupoEditar groupInfo;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;

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
        List<vistaDeNuevoGrupo> integrantesList = new ArrayList<>();

        // Crea y vincula el adaptador - integrantes
        CustomAdapterNuevoGrupoIntegrantes adapter = new CustomAdapterNuevoGrupoIntegrantes(this, integrantesList, idUsuario, idgrupo, recyclerViewIntegrantes);
        recyclerViewIntegrantes.setAdapter(adapter);

        //Configuracion del administrador de diseño - integrantes
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewIntegrantes.setLayoutManager(layoutManager);

        // Obtiene la informacion del servidor
        String url = "https://phpclusters-156700-0.cloudclusters.net/obtenerInformacionGrupoEditar.php";
        progressDialog.show();
        new InformacionGrupoEditarAsyncTask(this).execute(url, String.valueOf(idgrupo));

        //Obtiene la lista de integrantes
        String ulrIntegranes = "https://phpclusters-156700-0.cloudclusters.net/obtenerIntegrantesGrupo.php";
        new FetchMemberDetailsEditarAsyncTask(ActivityEditarGrupo.this, adapter, progressDialog, 2).execute(ulrIntegranes, String.valueOf(idgrupo));

        // Listeners para botones de atras
        imagebuttonAtras.setOnClickListener(new View.OnClickListener() {
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

        //Listener para cambiar imagen
        imagebuttonEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        //Listener para boton Guardar Cambios
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDescripcion.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityEditarGrupo.this, "Porfavor Ingrese una Descripción para el Grupo", Toast.LENGTH_SHORT).show();
                    txtDescripcion.setError("Ingrese una Descripción");
                } else {
                    // Show confirmation dialog
                    ConfirmationDialog.showConfirmationDialog(
                            ActivityEditarGrupo.this, // Replace with your activity or fragment context
                            "Confirmación",
                            "¿Está seguro de que desea realizar estos cambios?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario hace click en si
                                    updateGrupo();
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
            }
        });
    }

    @Override
    public void onDataFetched(List<informacionGrupoEditar> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            groupInfo = dataList.get(0);

            textviewNombreGrupo.setText(groupInfo.getNombre());
            imagebuttonEditarFoto.setImageBitmap(groupInfo.getImage());
            txtDescripcion.setText(groupInfo.getDescripcion());

            if (groupInfo.getVisualizacion() == 1) {
                checkTipoGrupo.setChecked(true);
            } else {
                checkTipoGrupo.setChecked(false);
            }
        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Actualiza el imageview para colocar la imagen seleccionada
            ImageView imageView = findViewById(R.id.imageview_EditarGrupoSubir2);
            imageView.setImageURI(selectedImageUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                imgPerfilByteArray = getBytes(inputStream);
                // La imagen ahora se encuentra en forma de byte array dentro de la variable imgPerfilByteArray para poder guardarse en la base de datos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == REQUEST_CODE) || (requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickImage();
            } else {
                showPermissionExplanation();
            }
        }
    }

    //metodo para revisar los permisos
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(ActivityEditarGrupo.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityEditarGrupo.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(ActivityEditarGrupo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityEditarGrupo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        }
    }

    //metodo para escojer la imagen
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Funcion para otorgar permisos manualmente
    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu galería y seleccionar una imagen, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abre los ajustes de la app para que el usuario pueda otorgar permiso
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Controlla decision negativa
            }
        });
        builder.show();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //funcion que controla el update
    private void updateGrupo() {
        //Obtiene estado del checkbox
        if (checkTipoGrupo.isChecked()) {
            visualizacion = 1;
        } else {
            visualizacion = 0;
        }

        //obtener la imagen del imageview y convertirlo a byte array
        Drawable drawable = imagebuttonEditarFoto.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imgPerfilByteArray = stream.toByteArray();


        //combierte la imagen a base64
        String base64Image = Base64.encodeToString(imgPerfilByteArray, Base64.DEFAULT);

        //obtiener el url de la imagen actual para poder ser eliminada
        urlAnterior = groupInfo.getUrlAnterior();

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("descripcion", txtDescripcion.getText().toString());
            jsonData.put("visualizacion", visualizacion);
            jsonData.put("imagen", base64Image);
            jsonData.put("urlanterior", urlAnterior);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //llama el asynctask
        new UpdateGrupoAsyncTask(ActivityEditarGrupo.this).execute(jsonData.toString());
    }
}