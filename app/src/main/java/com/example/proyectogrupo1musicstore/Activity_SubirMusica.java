package com.example.proyectogrupo1musicstore;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.vistaMusicaVideo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_SubirMusica extends AppCompatActivity {
    RecyclerView listas;
   // ListView listas;
    DrawerLayout drawerLayouts;
    ImageButton botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos;
    ImageView imageViewBuscarArchivos;
    CardView buscars, videos,seleccionarAudio;
    private static final int PICK_AUDIOS_REQUEST = 1;// Código de solicitud para seleccionar un archivo de audio
    private static final int REQUEST_CODE = 123;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_musica);


        // Obtén la referencia al Firebase Storage

        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusica);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layout);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtras);
        textviewAtrass = (TextView) findViewById(R.id.textview_GrupoPrincipalbotAtras);
        txtBuscarArchivos = (TextView) findViewById(R.id.BuscarArchivo);
        imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusqueda);
        buscars = (CardView) findViewById(R.id.cardViewBuscarArchivo);
        seleccionarAudio = (CardView) findViewById(R.id.cardViewSubirMusica);
        videos = (CardView) findViewById(R.id.cardViewNavegacionVideo);
        //subida

        //Creacion de una lista de elementos de vistaArchivos
        List<vistaMusicaVideo> dataList = new ArrayList<>();


        //Configuracion del administrador de disenio y adaptador para el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Use an appropriate layout manager
        listas.setLayoutManager(layoutManager);
        CustomAdapterMusicaVideos adapter = new CustomAdapterMusicaVideos(this, dataList);
        listas.setAdapter(adapter);

//Modificados
        seleccionarAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermission();
            }
        });

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId() == R.id.cardViewNavegacionVideo) {
                    actividad = Activity_SubirVideo.class;
                }
                if (view.getId() == R.id.cardViewBuscarArchivo) {
                    actividad = Activity_BuscarMusica.class;
                }
                if (view.getId() == R.id.btn_PrincipalAtras) {
                    actividad = ActivityArchivosPersonales.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        buscars.setOnClickListener(buttonClick);
        videos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);
        //comentario
    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    private void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionExplanation();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            pickAudioFromGallery();
        }
    }

    private void pickAudioFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_AUDIOS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickAudioFromGallery();
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
        builder.setMessage("Para acceder a tu archivos y seleccionar un audio en especifico, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIOS_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri audioUri = data.getData();
            // Convierte la URI en una ruta de archivo real, si es necesario
            String audioPath = getRealPathFromURI(getApplicationContext(),audioUri);
            // Llama al método que convierte el audio a base 64
            String audioBase64 = convertirAudioBase64(audioPath);
            Log.d("Probando: ", audioPath);
            if (audioBase64 != null) {
                Log.d("Base64Audio", audioBase64);
            } else {
                Log.d("Base64Audio", "El audio en Base64 es null");
            }
            //playAudioPreview(audioUri);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /*Metodo de prueba para reproducir el audio que seleccionamos
    private void playAudioPreview(Uri audioUri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, audioUri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            // Podrías detener la reproducción después de algunos segundos
            // O permitir que el usuario detenga la vista previa con un botón

        } catch (IOException e) {
            // Manejo de error
        }
    }*/

    /*Convertir el audio a base64 a partir de su URL para posteriormente subirlo a firebase por medio de volley y PHP*/
    private String convertirAudioBase64(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(filePath);
            byte[] audioBytes;
            if (inputStream != null) {
                audioBytes = new byte[inputStream.available()];
                inputStream.read(audioBytes);
                return Base64.encodeToString(audioBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*Hasta aqui*/

}