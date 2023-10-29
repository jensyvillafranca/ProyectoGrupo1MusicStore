package com.example.proyectogrupo1musicstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoDetalles;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityNuevoGrupoDetalles extends AppCompatActivity {

    RecyclerView lista;
    ImageButton botonAtras;
    TextView textviewAtras;
    EditText textNombreGrupo, textDescripcion;
    ImageView imgAgragarImagen;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo_detalles);

        // Inicialización de vistas y elementos del diseño
        lista = (RecyclerView) findViewById(R.id.recyclerview_NuevoGrupoDetalles);
        botonAtras = (ImageButton) findViewById(R.id.btn_NuevoGrupoDetallesAtras);
        textviewAtras = (TextView) findViewById(R.id.textview_NuevoGrupoDetallesBotAtras);
        textNombreGrupo = (EditText) findViewById(R.id.text_NuevoGrupoDetallesNombreGrupo);
        textDescripcion = (EditText) findViewById(R.id.text_NuevoGrupoDetallesDescripcion);
        imgAgragarImagen = (ImageView) findViewById(R.id.imageview_NuevoGrupoDetallesSubir);

        // Creación de una lista de elementos de vistaDeNuevoGrupo
        List<vistaDeNuevoGrupo> dataList = new ArrayList<>();
        dataList.add(new vistaDeNuevoGrupo("Usuario 1", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 2", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 3", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 4", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 5", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 6", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 7", R.drawable.iconodeusuariossinfoto));
        dataList.add(new vistaDeNuevoGrupo("Usuario 8", R.drawable.iconodeusuariossinfoto));

        // Configuración del administrador de diseño y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        CustomAdapterNuevoGrupoDetalles adapter = new CustomAdapterNuevoGrupoDetalles(this, dataList);
        lista.setAdapter(adapter);

        // Listener para manejar los botones de "Atrás"
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.btn_NuevoGrupoDetallesAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (view.getId() == R.id.textview_NuevoGrupoDetallesBotAtras) {
                    actividad = ActivityGrupoPrincipal.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

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

        imgAgragarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActivityNuevoGrupoDetalles.this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(ActivityNuevoGrupoDetalles.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                } else {
                    // Create an intent to pick an image from the gallery
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            }
        });

        // Asigna los listeners a los botones de "Atrás"
        botonAtras.setOnClickListener(buttonClick);
        textviewAtras.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
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
                byte[] byteArray = getBytes(inputStream);
                // La imagen ahora se encuentra en forma de byte array dentro de la variable byteArray para poder guardarse en la base de datos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                showPermissionExplanation();
            }
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
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the app settings so the user can grant the permission
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to cancel the action
                // You can close the app or take other actions here
            }
        });
        builder.show();
    }

}