package com.example.proyectogrupo1musicstore.Activities.Multimedia;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.Activity_SubirVideoAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.BuscarVideoAsyncTask;
import com.example.proyectogrupo1musicstore.Models.videoItem;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.FileUtils;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.VideoAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    public static String idPlayList = "1";
    public static String idFavorito = "1";
    public static int idUsuario;
    public static String nombreVideo, autor, genero,imagenPortadaBase64,duracion, fechaLanzamiento;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;
    private static final int PICK_VIDEOS_REQUEST = 1;// Código de solicitud para seleccionar un archivo de video
    RecyclerView recyclerViewVideos;
    Uri videoUri;
    public static byte[] videoBase64;

    byte[] imagenPortadaV;
    Bitmap imagenPortada_BitmapV;

    /*Contar cuantos elementos se necesitarán ingresar de la metadata del archivo*/
    public static int contadorElementosV =0;

    ProgressDialog progressDialog;
    public static final String tipo = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_video);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        //Inicializacion de vista y elementos.
        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusicavideos);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layouts);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtrasBottons);
        textviewAtrass = (TextView) findViewById(R.id.textview_PrincipalbotAtrastexto);
        //txtBuscarArchivos = (TextView) findViewById(R.id.txtbusquedaPrincipalActualizarvideos);
     //   imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusquedavideos);
        btnPrincipalAudio = (CardView) findViewById(R.id.cardViewNavegacionMusicavideoos);
        seleccionarVideos = (CardView) findViewById(R.id.cardViewSubirMusicaVideos);
       // busquedaVideos = (CardView) findViewById(R.id.cardViewBuscarArchivovideos);
        recyclerViewVideos = (RecyclerView) findViewById(R.id.recyclerview_SubirMusicavideos);

        seleccionarVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVideoPermission() ;
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

                if (view.getId() == R.id.btn_PrincipalAtrasBottons) {
                    actividad = Activity_SubirMusica.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        btnPrincipalAudio.setOnClickListener(buttonClick);
      //  busquedaVideos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);
        videoItems();
    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    private void requestVideoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 10 y superior, solicitar READ_MEDIA_VIDEO
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE);
            } else {
                // El permiso está concedido, proceder a seleccionar un video
                pickVideoFromGallery();
            }
        } else {
            // Android 9 y inferior, solicitar WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // El permiso está concedido, proceder a seleccionar un video
                pickVideoFromGallery();
            }
        }
    }

    private void pickVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEOS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso es concedido, proceder a seleccionar un video
                pickVideoFromGallery();
            } else {
                // Si el permiso es denegado, mostrar una explicación
                showPermissionExplanation();
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEOS_REQUEST && resultCode == RESULT_OK && data != null) {
            // Ubicación del recurso de video
            videoUri = data.getData();
            obtenerRutaRealVideo(getApplicationContext(), videoUri);
            // Mandar a llamar el método que procesa la metadata del video
            try {
                extraerMetadata();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String obtenerRutaRealVideo(Context context, Uri contentUri) {
        // Log.d("ENTRA", "ENTRA XDXDXD AL OBTENER RUTA REAL VIDEO");
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String filePath = cursor.getString(column_index);
                InputStream inputStream = new FileInputStream(filePath); // Abre el archivo como InputStream
                //Log.e("EL DE AHORITA: ",""+inputStream);
                videoBase64 = getBytes(inputStream); // Convierte a arreglo de bytes
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

    /*Convertir el video en base 64 mandando la ruta real desde el metodo de la parte de arriba*/

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


    public void subirVideoFirebase() throws IOException {
        Log.d("Mensaje", "se mandaron los datos");
        new Activity_SubirVideoAsyncTask(Activity_SubirVideo.this,autor, idUsuario, videoBase64 , imagenPortadaBase64, idPlayList, idFavorito, nombreVideo, genero, duracion, fechaLanzamiento).execute();
    }

    public void extraerMetadata() throws IOException {
        /*Clase que nos permitira obtener la metadata de ese video*/
        MediaMetadataRetriever metadata = new MediaMetadataRetriever();
        try {
            metadata.setDataSource(getApplicationContext(), videoUri);
            nombreVideo = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            autor = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            genero = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            duracion = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            fechaLanzamiento = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
            imagenPortadaV = metadata.getEmbeddedPicture();
            //Log.e("NOMBRE CANCION 1",""+nombreCancion);
            //Log.e("ARTISTA 1",""+artista);
            //Log.e("ALBUM 1",""+album);
            //Log.e("GENERO 1",""+genero);

            if (imagenPortadaV != null) {
                // Si el video tiene una imagen de portada
                imagenPortada_BitmapV = BitmapFactory.decodeByteArray(imagenPortadaV, 0, imagenPortadaV.length);
                // Convertir el Bitmap a un array de bytes
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagenPortada_BitmapV.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imagenPortadaBytes = byteArrayOutputStream.toByteArray();
                // Convertir el array de bytes a Base64
                imagenPortadaBase64 = Base64.encodeToString(imagenPortadaBytes, Base64.DEFAULT);
            } else {
                // Si no hay imagen de portada, asignamos nulo el bitmap
                imagenPortadaBase64 = null;
            }

            //Si el nombre del video viene vacío
            if(nombreVideo == null){
                contadorElementosV++;
            }
            //Si el autor del video viene vacío
            if(autor == null){
                contadorElementosV++;
            }
            //Si el género del video viene vacío
            if(genero == null){
                contadorElementosV++;
            }
            //Si la duración del video viene vacío
            if(duracion == null){
                contadorElementosV++;
            }
            //Si la fecha de lanzamiento del video viene vacío
            if(fechaLanzamiento == null){
                contadorElementosV++;
            }
            //Log.d("CONTADOR", ""+ contadorElementosV);

            //Mandar a llamar la modal donde el usuario digitara la información que esta nula de la metadata.
            if(contadorElementosV > 0 && contadorElementosV < 7){
                activity_personalizada_metadata_video dialogFragment = activity_personalizada_metadata_video.newInstance(this, videoUri);
                dialogFragment.show(getSupportFragmentManager(), "ventana");
                /*Una vez que la data este llena, en la otra clase de la modal se mandara a instanciar esta clase para
                 * acceder al método de subirAudioFirebase*/
            }else if(contadorElementosV == 0){
                //En caso de que no existan valores en null, seguimos con el proceso inicial: insertar.
                //Mandar a llamar el metodo que manda toda la data a la clase Activity_SubirVideoAsyncTask
                try {
                    Log.d("ENTRA A ESTO","ENTRA A ESTO");
                    subirVideoFirebase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            metadata.release(); // Liberar el recurso
        }
        contadorElementosV = 0;
        /*Llamar la clase que permite traer todos los archivos musicales*/
        videoItems();
    }

    public void videoItems(){
        //Obteniendo el id del usuario por medio del token
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        // Creación de una lista de elementos para los audios
        List<videoItem> videoList = new ArrayList<>();

        // Crea y vincula el adaptador
        VideoAdapter videoAdapter = new VideoAdapter(this, videoList);
        recyclerViewVideos.setAdapter(videoAdapter);

        //Configuracion del administrador de diseño
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewVideos.setLayoutManager(layoutManager);
        new BuscarVideoAsyncTask(getApplicationContext(), videoAdapter, progressDialog).execute(String.valueOf(idUsuario), tipo);
    }
}