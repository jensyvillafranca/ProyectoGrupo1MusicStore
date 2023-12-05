package com.example.proyectogrupo1musicstore.Activities.Grupos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoDetalles;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.CreateGroupAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.FetchMemberDetailsAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityNuevoGrupoDetalles extends AppCompatActivity implements FetchMemberDetailsAsyncTask.DataFetchListener{

    RecyclerView lista;
    ImageButton botonAtras;
    TextView textviewAtras;
    EditText textNombreGrupo, textDescripcion;
    ImageView imgAgragarImagen;
    List<Integer> selectedUserIds;
    CheckBox checkPrivado;
    Button btnUnirse;
    byte[] imgPerfilByteArray;
    private int estadoPrivado;
    ProgressDialog progressDialog;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo_detalles);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        try{
            selectedUserIds = getIntent().getIntegerArrayListExtra("selectedUserIds");
        }catch (Exception e){
            Log.e("Error", "Lista se usuarios vacia");
        }



        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_NuevoGrupoDetalles);
        botonAtras = (ImageButton) findViewById(R.id.btn_NuevoGrupoDetallesAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_NuevoGrupoDetallesBotAtras);
        textNombreGrupo = (EditText) findViewById(R.id.text_NuevoGrupoDetallesNombreGrupo);
        textDescripcion = (EditText) findViewById(R.id.text_NuevoGrupoDetallesDescripcion);
        imgAgragarImagen = (ImageView) findViewById(R.id.imageview_NuevoGrupoDetallesSubir);
        checkPrivado = (CheckBox) findViewById(R.id.checkboxNuevoGrupoDetalles);
        btnUnirse = (Button) findViewById(R.id.btnNuevoGrupoDetallesUnirse);

        // Creación de una lista de elementos de vistaDeNuevoGrupo
        List<vistaDeNuevoGrupo> dataList = new ArrayList<>();

        if(selectedUserIds.size()<1){
            progressDialog.dismiss();
        }else{
            fetchMemberDetails(selectedUserIds, dataList);
        }

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);


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

        //Listener para manejar el cierre del teclado con el boton de enter
        textNombreGrupo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //Listener para manejar el cierre del teclado con el boton de enter
        textDescripcion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //Listener para el boton de agregar imagen
        imgAgragarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        //Listener para el boton unirse
        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgPerfilByteArray == null){
                    Toast.makeText(ActivityNuevoGrupoDetalles.this, "Porfavor Seleccione una Imagen para el Grupo", Toast.LENGTH_SHORT).show();
                } else if (textNombreGrupo.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityNuevoGrupoDetalles.this, "Porfavor Ingrese un Nombre para el Grupo", Toast.LENGTH_SHORT).show();
                    textNombreGrupo.setError("Ingrese un Nombre");
                } else if(textDescripcion.getText().toString().isEmpty()){
                    Toast.makeText(ActivityNuevoGrupoDetalles.this, "Porfavor Ingrese una Descripción para el Grupo", Toast.LENGTH_SHORT).show();
                    textDescripcion.setError("Ingrese una Descripción");
                }
                else{
                    // Show confirmation dialog
                    ConfirmationDialog.showConfirmationDialog(
                            ActivityNuevoGrupoDetalles.this,
                            "Confirmación",
                            "¿Está seguro de que desea crear el grupo?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Si el usuario hace click en si
                                    new CreateGroupAsyncTask(ActivityNuevoGrupoDetalles.this, selectedUserIds, textNombreGrupo.getText().toString(), textDescripcion.getText().toString(), imgPerfilByteArray, estadoPrivado, textNombreGrupo, idUsuario).execute();
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

        //Listener para el checkbox
        checkPrivado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Si el checkbox esta seleccionado
                estadoPrivado = 1;
            } else {
                // Si el checkbox no esta seleccionado
                estadoPrivado = 0;
            }
        });
    }

    //override al metodo para procesar la seleccion de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Actualiza el imageview para colocar la imagen seleccionada
            ImageView imageView = findViewById(R.id.imageview_NuevoGrupoDetallesSubir);
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

        if ((requestCode == REQUEST_CODE)||(requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickImage();
            } else {
                showPermissionExplanation();
            }
        }
    }

    @Override
    public void onDataFetched(List<vistaDeNuevoGrupo> dataList) {
        // Muestra el Recycle view con la nueva informacion
        if (dataList.size() == selectedUserIds.size()) {
            progressDialog.dismiss(); // Esconde el spinner de carga
            CustomAdapterNuevoGrupoDetalles adapter = new CustomAdapterNuevoGrupoDetalles(this, dataList);
            lista.setAdapter(adapter);
        }
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

    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu galería y seleccionar una imagen, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
        builder.setPositiveButton("Ir a Ajustes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abre los ajustes de la app para que el usuario pueda otorgar permiso
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Controlla decision negativa
            }
        });
        builder.show();
    }

    private void fetchMemberDetails(List<Integer> userIds, List<vistaDeNuevoGrupo> dataList) {
        for (Integer userId : userIds) {
            // Fetch data from the server
            String idUsuarios = userId.toString();
            String url = "https://phpclusters-156700-0.cloudclusters.net/buscarIntegrantePorID.php";
            new FetchMemberDetailsAsyncTask(this, dataList, 1).execute(url, idUsuarios);
        }
    }

    //metodo para escojer la imagen
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //metodo para revisar los permisos
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(ActivityNuevoGrupoDetalles.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityNuevoGrupoDetalles.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(ActivityNuevoGrupoDetalles.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityNuevoGrupoDetalles.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        }
    }
}