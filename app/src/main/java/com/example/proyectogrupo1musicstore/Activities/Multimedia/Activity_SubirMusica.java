package com.example.proyectogrupo1musicstore.Activities.Multimedia;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.Activity_SubirMusicaAsyncTask;
import com.example.proyectogrupo1musicstore.Adapters.AudioAdapter;
import com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia.BuscarAudioAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.FileUtils;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.Models.audioItem;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Activity_SubirMusica extends AppCompatActivity {
    RecyclerView listas;
    RecyclerView recyclerViewAudios;
    DrawerLayout drawerLayouts;
    ImageButton botonAtrass;
    TextView textviewAtrass, txtBuscarArchivos;
    private Button btnPlays, btnPrevButtons, btnForwardButtons;
    ImageView imageViewBuscarArchivos;
    CardView buscars, videos, seleccionarAudio;
    private static final int PICK_AUDIOS_REQUEST = 1;// Código de solicitud para seleccionar un archivo de audio
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    public static byte[] audioBase64;
    Uri audioUri;
    public static String nombreCancion, artista,album,genero,imagenPortadaBase64;
    public static int idUsuario;
    public static String idPlayList = "1";
    public static String idFavorito = "1";
    byte[] imagenPortada;
    Bitmap imagenPortada_Bitmap;

    /*Contar cuantos elementos se necesitarán ingresar de la metadata del archivo*/
    public static int contadorElementos=0;

    /*Generar de ID único para los plain text en los que se va llenar la info faltante de la metadata*/
    private static final AtomicInteger generarID = new AtomicInteger(1);

    ProgressDialog progressDialog;
    public static final String tipo = "1";
    //Modificacion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_musica);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        listas = (RecyclerView) findViewById(R.id.recyclerview_SubirMusica);
        drawerLayouts = (DrawerLayout) findViewById(R.id.drawer_layout);
        botonAtrass = (ImageButton) findViewById(R.id.btn_PrincipalAtras);
        textviewAtrass = (TextView) findViewById(R.id.textview_GrupoPrincipalbotAtras);
        //txtBuscarArchivos = (TextView) findViewById(R.id.BuscarArchivo);
        //imageViewBuscarArchivos = (ImageView) findViewById(R.id.imageviewBusqueda);
        //buscars = (CardView) findViewById(R.id.cardViewBuscarArchivo);
        seleccionarAudio = (CardView) findViewById(R.id.cardViewSubirMusica);
        videos = (CardView) findViewById(R.id.cardViewNavegacionVideo);
        recyclerViewAudios = (RecyclerView) findViewById(R.id.recyclerview_SubirMusica);

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

                if (view.getId() == R.id.btn_PrincipalAtras) {
                    actividad = ActivityPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
       // buscars.setOnClickListener(buttonClick);
        videos.setOnClickListener(buttonClick);
        botonAtrass.setOnClickListener(buttonClick);
        musicaItems();
    }
    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    private void requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
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
            obtenerRutaRealMusica(getApplicationContext(), audioUri);
            /*Mandar a llamar el metodo que captura la metadata del archivo de audio*/
            try {
                extraerMetadata();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*Este metódo convierte la Uri en una ruta de archivo real en el sistema de archivos del dispositivo*/
    /*Es útil porque gracias a él podemos hacer operaciones de lectura y escritura, en nuestro caso necesitamos leerlo
    * para convertirlo a base 64 y también para la metadata del audio*/
    public String obtenerRutaRealMusica(Context context, Uri contentUri) {
       // Log.d("ENTRA", "ENTRA XDXDXD AL OBTENER RUTA REAL MUSICA");
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String filePath = cursor.getString(column_index);
                InputStream inputStream = new FileInputStream(filePath); // Abre el archivo como InputStream
                //Log.e("EL DE AHORITA: ",""+inputStream);
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
        Log.d("Mensaje", "se mandaron los datos");
        new Activity_SubirMusicaAsyncTask(Activity_SubirMusica.this,artista, idUsuario, audioBase64 , imagenPortadaBase64, idPlayList, idFavorito, nombreCancion, album, genero).execute();
    }

    /*Metodo para obtener metadata del archivo de música*/
    /*Obtener la metada del archivo de audio*/
    public void extraerMetadata() throws IOException {
        /*Clase que nos permitira obtener la metadata de ese audio*/
        MediaMetadataRetriever metadata = new MediaMetadataRetriever();
        //Log.d("ENTRA", "ENTRA XDXDXD A METADATA");
        try {
            metadata.setDataSource(getApplicationContext(), audioUri);
            nombreCancion = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            artista = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            genero = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            imagenPortada = metadata.getEmbeddedPicture();
            Log.e("NOMBRE CANCION 1",""+nombreCancion);
            Log.e("ARTISTA 1",""+artista);
            Log.e("ALBUM 1",""+album);
            Log.e("GENERO 1",""+genero);

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
                imagenPortadaBase64 = null;
            }

            //Si el nombre de la canción viene vacío
            if(nombreCancion == null){
                contadorElementos++;
            }
            //Si el artista de la canción viene vacío
            if(artista == null){
                contadorElementos++;
            }
            //Si el albúm de la canción viene vacío
            if(album == null){
                contadorElementos++;
            }
            //Si el género de la canción viene vacío
            if(genero == null){
                contadorElementos++;
            }
            Log.d("CONTADOR", ""+contadorElementos);

            //Mandar a llamar la modal donde el usuario digitara la información que esta nula de la metadata.
            if(contadorElementos > 0 && contadorElementos < 5){
                activity_personalizada_metadata dialogFragment = activity_personalizada_metadata.newInstance(this, audioUri);
                dialogFragment.show(getSupportFragmentManager(), "ventana");
                /*Una vez que la data este llena, en la otra clase de la modal se mandara a instanciar esta clase para
                * acceder al método de subirAudioFirebase*/
            }else if(contadorElementos == 0){
                //En caso de que no existan valores en null, seguimos con el proceso inicial: insertar.
                //Mandar a llamar el metodo que manda toda la data a la clase Activity_SubirMusicaAsyncTask
                try {
                    Log.d("ENTRA A ESTO","ENTRA A ESTO");
                    subirAudioFirebase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            metadata.release(); // Liberar el recurso
        }
        contadorElementos = 0;
        /*Llamar la clase que permite traer todos los archivos musicales*/
        musicaItems();
    }

    /*Metodo para mostrar todas las canciones subidas en formato de grilla 2x2*/
    public void musicaItems(){
        //Obteniendo el id del usuario por medio del token
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        // Creación de una lista de elementos para los audios
        List<audioItem> audioList = new ArrayList<>();

        // Crea y vincula el adaptador
        AudioAdapter audioAdapter = new AudioAdapter(this, audioList );
        recyclerViewAudios.setAdapter(audioAdapter);

        //Configuracion del administrador de diseño
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewAudios.setLayoutManager(layoutManager);

        new BuscarAudioAsyncTask(getApplicationContext(), audioAdapter, progressDialog).execute(String.valueOf(idUsuario), tipo);
    }

}