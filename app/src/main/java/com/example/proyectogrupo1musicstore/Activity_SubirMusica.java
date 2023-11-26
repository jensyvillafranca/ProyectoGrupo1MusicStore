package com.example.proyectogrupo1musicstore;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.vistaMusicaVideo;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Activity_SubirMusica extends AppCompatActivity {
    RecyclerView listas;
    // ListView listas;
    DrawerLayout drawerLayouts;
    ImageButton botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos;
    ImageView imageViewBuscarArchivos;
    CardView buscars, videos, seleccionarAudio;
    private static final int PICK_AUDIOS_REQUEST = 1;// Código de solicitud para seleccionar un archivo de audio
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;
    private com.example.proyectogrupo1musicstore.Utilidades.token token = new token(this);

    private int idUsuario;
    private String idPlayList = "1";
    private String idFavorito = "1";

    byte[] audioBase64;

    /*Clase que nos permitira obtener la metadata de ese audio*/
    MediaMetadataRetriever metadata = new MediaMetadataRetriever();
    Uri audioUri;
    String nombreCancion, artista,album,genero,duracion, imagenPortadaBase64;
    byte[] imagenPortada;
    Bitmap imagenPortada_Bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_musica);

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

        //Obteniendo el id del usuario por medio del token
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

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
                    actividad = ActivityPlayList.class;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickAudioFromGallery();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickAudioFromGallery();
            }
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
            //Ubicación del recurso de audio
            audioUri = data.getData();
            //Pasar todos los parametros que la clase Activity_SubirMusicaAsyncTask necesita para mandar al PHP
            obtenerRutaRealMusica(getApplicationContext(), audioUri);
            //Mandar a llamar el metodo que manda toda la data a la clase Activity_SubirMusicaAsyncTask
            try {
                subirAudioFirebase();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*Este metódo convierte la Uri en una ruta de archivo real en el sistema de archivos del dispositivo*/
    /*Es útil porque gracias a él podemos hacer operaciones de lectura y escritura, en nuestro caso necesitamos leerlo
    * para convertirlo a base 64 y también para la metadata del audio*/
    public String obtenerRutaRealMusica(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String filePath = cursor.getString(column_index);
                InputStream inputStream = new FileInputStream(filePath); // Abre el archivo como InputStream
                audioBase64 = getBytes(inputStream); // Convierte a arreglo de bytes
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de excepciones
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /*Convertir el audio en base 64 mandando la ruta real desde el metodo de la parte de arriba*/

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



    public void subirAudioFirebase() throws IOException {
        extraerMetadata();
        new Activity_SubirMusicaAsyncTask(getApplicationContext(),artista, idUsuario, audioBase64 , imagenPortadaBase64, idPlayList, idFavorito, nombreCancion, album, genero);
        //idPlayList preguntar a Jonathan
        //idFavorito resolver también
    }

    /*Metodo para obtener metadata del archivo de música*/
    /*Obtener la metada del archivo de audio*/
    public void extraerMetadata() throws IOException {
        try {
            metadata.setDataSource(getApplicationContext(), audioUri);
            nombreCancion = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            artista = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            genero = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
           // duracion = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            imagenPortada = metadata.getEmbeddedPicture();

            if (imagenPortada != null) {
                // Si la canción tiene una imagen de portada
                imagenPortada_Bitmap = BitmapFactory.decodeByteArray(imagenPortada, 0, imagenPortada.length);
                // Convertir el Bitmap a un array de bytes
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagenPortada_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imagenPortadaBytes = byteArrayOutputStream.toByteArray();
                // Convertir el array de bytes a Base64
                imagenPortadaBase64 = Base64.encodeToString(imagenPortadaBytes, Base64.DEFAULT);
            } else {
                // Si no hay imagen de portada, asignamos nulo el bitmap
                imagenPortadaBase64 = "null";
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            metadata.release(); // No olvides liberar el recurso
        }
    }




}