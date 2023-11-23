package com.example.proyectogrupo1musicstore;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.vistaMusicaVideo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Activity_SubirVideo extends AppCompatActivity {

    RecyclerView listas;
    DrawerLayout drawerLayouts;
    ImageButton openMenuButtons, botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos, Archivoss, Inicios;
    ImageView imageViewBuscarArchivos, iconoArchivos,iconInicios;
    CardView buscars,busquedaVideos, seleccionarVideos, btnPrincipalAudio;
    private static final int PICK_VIDEOS_REQUEST = 1;// Código de solicitud para seleccionar un archivo de audio
    private static final int REQUEST_CODE = 123;
    // Inicialización de Firebase Storage


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_video);

        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusicavideos);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layouts);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtrasBottons);
        textviewAtrass = (TextView) findViewById(R.id.textview_PrincipalbotAtrastexto);
        txtBuscarArchivos = (TextView) findViewById(R.id.txtbusquedaPrincipalActualizarvideos);
        imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusquedavideos);
        btnPrincipalAudio = (CardView) findViewById(R.id.cardViewNavegacionMusicavideoos);
        seleccionarVideos = (CardView) findViewById(R.id.cardViewSubirMusicaVideos);
        busquedaVideos = (CardView) findViewById(R.id.cardViewBuscarArchivovideos);

        //Creacion de una lista de elementos de vistaArchivos
        List<vistaMusicaVideo> dataList = new ArrayList<>();

        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

        seleccionarVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Activity_SubirVideo.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(Activity_SubirVideo.this, new String[]{Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE);
                } else {
                    // Create an intent to pick an image from the gallery
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_VIDEOS_REQUEST);
                }
            }
        });
        //subida
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.cardViewNavegacionMusicavideoos) {
                    actividad = Activity_SubirMusica.class;
                }
                if (view.getId() == R.id.cardViewBuscarArchivovideos) {
                    actividad = Activity_BuscarVideos.class;
                }
                if (view.getId() == R.id.btn_PrincipalAtrasBottons) {
                    actividad = Activity_SubirMusica.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        btnPrincipalAudio.setOnClickListener(buttonClick);
        busquedaVideos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);

    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an audio from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_VIDEOS_REQUEST);
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
        builder.setMessage("Para acceder a tu archivos y seleccionar un video en específico, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación...");
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