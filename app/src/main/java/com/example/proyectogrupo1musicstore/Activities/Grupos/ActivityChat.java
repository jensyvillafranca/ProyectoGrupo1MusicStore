package com.example.proyectogrupo1musicstore.Activities.Grupos;

import static kotlin.io.ByteStreamsKt.readBytes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.proyectogrupo1musicstore.Adapters.AudioPersonalAdapter;
import com.example.proyectogrupo1musicstore.Adapters.GruposFavoritosAdapter;
import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Adapters.MessageAdapter;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
import com.example.proyectogrupo1musicstore.Models.mensajeModel;
import com.example.proyectogrupo1musicstore.Models.musicItem;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.enviarMensajeAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerAudiosPersonalesAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.obtenerIntegrantesGrupoAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.FileUtils;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloaderAsync;
import com.example.proyectogrupo1musicstore.Utilidades.Nombramiento.UniqueFileNameGenerator;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityChat extends AppCompatActivity implements MessageAdapter.OnPlayClickListener{

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private DatabaseReference messagesRef;
    private token acceso = new token(this);
    private int idUsuario;
    private int idgrupo;
    private String image, nombreGrupo, audioFilePath;
    private ImageButton btnAtras, btnEnviar, btnUpload, closePlayer;
    private TextView textviewNombreGrupo;
    private ImageView imgGrupo;
    private EditText mensaje;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private SeekBar seekBar;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private Handler handler;
    private GestureDetector gestureDetector;
    private Uri voiceMessage;
    private ProgressDialog progressDialog;
    private static final int DELAY_START_RECORDING = 1000;

    private static final int PICK_AUDIO_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_VIDEO = 126;
    private static final int REQUEST_CODE_EXTERNAL = 124;
    private static final int REQUEST_AUDIO_SELECTION = 127;
    private static final int REQUEST_VIDEO_SELECTION = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        idgrupo = getIntent().getIntExtra("idgrupo", 0);
        nombreGrupo = getIntent().getStringExtra("nombregrupo");
        image = getIntent().getStringExtra("image");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Grabando...");
        progressDialog.setCancelable(false);

        //Audio Recorder
        handler = new Handler();
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        btnAtras = findViewById(R.id.backButton);
        btnEnviar = findViewById(R.id.sendButton);
        btnUpload = findViewById(R.id.uploadButton);
        textviewNombreGrupo = findViewById(R.id.groupName);
        imgGrupo = findViewById(R.id.groupImage);
        mensaje = findViewById(R.id.messageField);
        closePlayer = findViewById(R.id.exo_close);
        seekBar = findViewById(R.id.seekBar);

        //Asigna los valores
        textviewNombreGrupo.setText(nombreGrupo);

        //Baja la imagen y la coloca en el imageView
        ImageDownloaderAsync imageDownloaderAsync = new ImageDownloaderAsync(imgGrupo);
        imageDownloaderAsync.execute(image);

        //Inicia Recycler view
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        List<mensajeModel> messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(messages, String.valueOf(idUsuario), this);
        recyclerView.setAdapter(messageAdapter);

        messagesRef = FirebaseDatabase.getInstance().getReference("mensajes"+idgrupo);


        // Aplica el listener para mensajes
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                mensajeModel message = dataSnapshot.getValue(mensajeModel.class);
                Log.d("Firebase", "Received message: " + message.getText());
                messageAdapter.addMessage(message);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Scroll to the last item
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Other overridden methods for ChildEventListener...

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

        //Listeners para boton enviar
        btnEnviar.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true; // Consume the touch event
        });

        btnEnviar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);

                // Handle ACTION_UP event to stop recording
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                }

                return true; // Consume the touch event
            }
        });

        //Listener para imagen de grupo hacia info perfil
        imgGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityChat.this, ActivityGrupoInfo.class);
                intent.putExtra("idgrupo", idgrupo);
                startActivity(intent);
            }
        });

        //Listener boton de atras
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        closePlayer.setOnClickListener(view -> {
            releasePlayer();
            playerView.setVisibility(View.GONE);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Handle progress change, update player position if needed
                if (fromUser) {
                    // Calculate the position based on progress and update your player
                    // For example, if you have a player named 'exoplayer', you can do:
                    long duration = exoPlayer.getDuration();
                    long newPosition = (duration * progress) / 100;
                    exoPlayer.seekTo(newPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle tracking start
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle tracking stop, update player position
                // This might not be necessary, as you can update the position in onProgressChanged
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Handle the selected audio file URI
            Uri audioUri = data.getData();
            // Now, proceed to the next step (uploading the audio file)
            uploadAudioFile(audioUri, 1);
        }
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Handle the selected audio file URI
            Uri videoUri = data.getData();
            // Now, proceed to the next step (uploading the audio file)
            uploadVideoFile(videoUri);
        }
        if (requestCode == REQUEST_AUDIO_SELECTION && resultCode == RESULT_OK && data != null) {
            String selectedAudioUrl = data.getStringExtra("selectedAudioUrl");
            String audioName = data.getStringExtra("audioName");
            enviarMensajeAudioUrl(audioName, selectedAudioUrl);
        }
        if (requestCode == REQUEST_VIDEO_SELECTION && resultCode == RESULT_OK && data != null) {
            String selectedVideoUrl = data.getStringExtra("selectedVideoUrl");
            String videoName = data.getStringExtra("videoName");
            enviarMensajeVideoUrl(videoName, selectedVideoUrl);
        }
    }

    // Metodo para subir un archivo de musica -- tipo 1 = musica de telefono, tipo 2 = mensaje de voz
    private void uploadAudioFile(Uri audioUri, int tipo) {
        try {
            // Abre un InputStream from the audioUri
            InputStream inputStream = getContentResolver().openInputStream(audioUri);

            // Lee el InputStream
            byte[] audioBytes = readBytes(inputStream);

            // Convierte el byte array a Base64-encoded
            String base64Audio = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            // base64 listo para ser enviado
            if(tipo==1){
                // Get the file name from the URI
                String audioFileName = new FileUtils(this).getFileName(audioUri);
                enviarMensajeAudio(audioFileName, base64Audio);
            }else{
                String uniqueFileName = UniqueFileNameGenerator.generateUniqueFileName("mp4");
                enviarMensajeVoice(uniqueFileName, base64Audio);
            }

            // Close the InputStream
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    // Metodo para subir un archivo de video
    private void uploadVideoFile(Uri videoUri) {
        try {
            // Abre un InputStream from the audioUri
            InputStream inputStream = getContentResolver().openInputStream(videoUri);

            // Lee el InputStream
            byte[] audioBytes = readBytes(inputStream);

            // Convierte el byte array a Base64-encoded
            String base64Video = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            // base64 listo para ser enviado
            String videoFileName = new FileUtils(this).getFileName(videoUri);
            enviarMensajeVideo(videoFileName, base64Video);

            // Close the InputStream
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    // Implement the onPlayClick method
    @Override
    public void onPlayClick(String audioUrl) {
        // Use the mainHandler to ensure UI operations are on the main thread
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("AudioUrl", audioUrl);
                initializePlayer();
                MediaItem mediaItem = MediaItem.fromUri(audioUrl);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                exoPlayer.setPlayWhenReady(true);
            }
        });
    }

    // Inicia el reproductor
    private void initializePlayer(){
        if (exoPlayer == null) {
            // Create a SimpleExoPlayer instance
            exoPlayer = new SimpleExoPlayer.Builder(this).build();

            // Bind the player to the view
            playerView = findViewById(R.id.playerView);
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(exoPlayer);
        }
    }

    // Cierra el reproductor
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();

        //Audio Recorder
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
    }

    //Metodo para enviar el mensaje al asynctask
    private void enviarMensaje(){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", mensaje.getText().toString());
            jsonData.put("mediatype", "Ninguno");
            jsonData.put("media", null);
            jsonData.put("nombrearchivo", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(1));
    }

    //Metodo para enviar el mensaje con audio al asynctask
    private void enviarMensajeAudio(String audioFileName, String media){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", audioFileName);
            jsonData.put("mediatype", "Audio");
            jsonData.put("media", media);
            jsonData.put("nombrearchivo", audioFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JsonData", String.valueOf(jsonData));

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(1));
        Toast.makeText(this, "Audio Enviado", Toast.LENGTH_SHORT).show();
    }

    //Metodo para enviar mensaje con audio - solo url
    private void enviarMensajeAudioUrl(String audioFileName, String url){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", audioFileName);
            jsonData.put("mediatype", "Audio");
            jsonData.put("url", url);
            jsonData.put("nombrearchivo", audioFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JsonData", String.valueOf(jsonData));

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(2));
        Toast.makeText(this, "Audio Enviado", Toast.LENGTH_SHORT).show();
    }

    private void enviarMensajeVideo(String videoFileName, String media){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", videoFileName);
            jsonData.put("mediatype", "Video");
            jsonData.put("media", media);
            jsonData.put("nombrearchivo", videoFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JsonData", String.valueOf(jsonData));

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(1));
        Toast.makeText(this, "Video Enviado", Toast.LENGTH_SHORT).show();
    }

    //Metodo para enviar mensaje de video - solo url
    private void enviarMensajeVideoUrl(String videoFileName, String url){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", videoFileName);
            jsonData.put("mediatype", "Video");
            jsonData.put("url", url);
            jsonData.put("nombrearchivo", videoFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JsonData", String.valueOf(jsonData));

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(2));
        Toast.makeText(this, "Video Enviado", Toast.LENGTH_SHORT).show();
    }

    //Metodo para enviar mensaje de voz
    private void enviarMensajeVoice(String audioFileName, String media){

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", idgrupo);
            jsonData.put("idusuario", idUsuario);
            jsonData.put("mensaje", "Mensaje de Voz");
            jsonData.put("mediatype", "Voice");
            jsonData.put("media", media);
            jsonData.put("nombrearchivo", audioFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JsonData", String.valueOf(jsonData));

        //llama el asynctask
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString(), String.valueOf(3));
    }


    // Metodo para elegir el audio
    private void pickAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/mp4");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    public void showUploadOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.upload_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_upload_app_storage_music) {
                showAudioSelectionDialog();
                return true;
            } else if (itemId == R.id.menu_upload_app_storage_video) {
                showVideoSelectionDialog();
                return true;
            } else if (itemId == R.id.menu_upload_phone_storage_music) {
                checkPermissions();
                return true;
            }else if (itemId == R.id.menu_upload_phone_storage_video) {
                checkPermissionsVideo();
                return true;
            }
            else {
                return false;
            }
        });

        popupMenu.show();
    }

    //metodo para revisar los permisos
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(ActivityChat.this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityChat.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickAudio();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(ActivityChat.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityChat.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickAudio();
            }
        }
    }

    private void checkPermissionsVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(ActivityChat.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityChat.this, new String[]{Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickVideo();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(ActivityChat.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityChat.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickVideo();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == REQUEST_CODE)||(requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickAudio();
            } else {
                showPermissionExplanation();
            }
        }
        if ((requestCode == REQUEST_CODE_VIDEO)||(requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickVideo();
            } else {
                showPermissionExplanation();
            }
        }
    }

    // Metodo para pedir permiso manualmente
    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu galería y seleccionar un archivo, necesitamos el permiso de lectura. Por favor, otorga el permiso en la configuración de la aplicación..");
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

    private void showAudioSelectionDialog() {
        Intent intent = new Intent(this, ActivityAudioSelection.class);
        intent.putExtra("idusuario", idUsuario);
        startActivityForResult(intent, REQUEST_AUDIO_SELECTION);
    }

    private void showVideoSelectionDialog() {
        Intent intent = new Intent(this, ActivityVideoSelection.class);
        intent.putExtra("idusuario", idUsuario);
        startActivityForResult(intent, REQUEST_VIDEO_SELECTION);
    }


    private void startRecording() {
        try {
            // Set up the MediaRecorder
            progressDialog.show();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            // Set the output file path (you can customize this)
            audioFilePath = getExternalFilesDir(null).getAbsolutePath() + "/audio_record.mp4";
            mediaRecorder.setOutputFile(audioFilePath);

            // Prepara y comienza a guardar
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                btnEnviar.setImageResource(R.drawable.botonenviar);

                // Use FileProvider to get a content URI
                File audioFile = new File(audioFilePath);
                voiceMessage = FileProvider.getUriForFile(
                        this,
                        "com.example.proyectogrupo1musicstore.fileprovider",
                        audioFile
                );

                uploadAudioFile(voiceMessage, 2);
                progressDialog.dismiss();
                Toast.makeText(this, "Mensaje de Voz Enviado", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacksAndMessages(null);
    }

    // Clase para controlar tap o cuando se mantiene precionado el boton
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            // Start recording after a delay
            handler.postDelayed(() -> {
                btnEnviar.setImageResource(R.drawable.recordicon);
                startRecording();
            }, DELAY_START_RECORDING);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            botonenviarpress();
            return true;
        }


    }

    // Metodo para cuado se da click en enviar
    private void botonenviarpress(){
        if (mensaje.getText().toString().isEmpty()) {
            Toast.makeText(ActivityChat.this, "No se Aceptan Mensajes Vacíos", Toast.LENGTH_SHORT).show();
        } else {

            // Envia el mensaje
            enviarMensaje();

            // Limpia el chat
            mensaje.setText("");

            // Cierra el teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mensaje.getWindowToken(), 0);
        }
    }
}