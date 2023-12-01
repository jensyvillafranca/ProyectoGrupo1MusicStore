package com.example.proyectogrupo1musicstore.Activities.Grupos;

import static kotlin.io.ByteStreamsKt.readBytes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.proyectogrupo1musicstore.Adapters.MessageAdapter;
import com.example.proyectogrupo1musicstore.Models.mensajeModel;
import com.example.proyectogrupo1musicstore.NetworkTasks.enviarMensajeAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.FileUtils;
import com.example.proyectogrupo1musicstore.Utilidades.ImageDownloaderAsync;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String image;
    private String nombreGrupo;
    private ImageButton btnAtras, btnEnviar, btnUpload, closePlayer;
    private TextView textviewNombreGrupo;
    private ImageView imgGrupo;
    private EditText mensaje;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private SeekBar seekBar;

    private static final int PICK_AUDIO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        idgrupo = getIntent().getIntExtra("idgrupo", 0);
        nombreGrupo = getIntent().getStringExtra("nombregrupo");
        image = getIntent().getStringExtra("image");

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
                Log.e("Data: ", String.valueOf(dataSnapshot.getValue(mensajeModel.class)));
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

        //Listener boton enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
                mensaje.setText("");
                // Cierra el teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_AUDIO_REQUEST);
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
            uploadAudioFile(audioUri);
        }
    }

    private void uploadAudioFile(Uri audioUri) {
        // Get the file name from the URI
        String audioFileName = new FileUtils(this).getFileName(audioUri);

        try {
            // Open an InputStream from the audioUri
            InputStream inputStream = getContentResolver().openInputStream(audioUri);

            // Read the InputStream into a byte array
            byte[] audioBytes = readBytes(inputStream);

            // Convert the byte array to a Base64-encoded string
            String base64Audio = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            // Now, you can send this base64Audio to Firebase Realtime Database
            enviarMensajeAudio(audioFileName, base64Audio);

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
    }

    //Metodo para moverse de actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
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
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString());
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
        new enviarMensajeAsyncTask(ActivityChat.this).execute(jsonData.toString());
    }
}