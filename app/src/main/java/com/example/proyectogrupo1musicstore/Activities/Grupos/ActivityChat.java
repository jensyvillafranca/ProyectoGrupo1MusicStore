package com.example.proyectogrupo1musicstore.Activities.Grupos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.proyectogrupo1musicstore.Adapters.MessageAdapter;
import com.example.proyectogrupo1musicstore.Models.mensajeModel;
import com.example.proyectogrupo1musicstore.NetworkTasks.enviarMensajeAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.ImageDownloaderAsync;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityChat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private DatabaseReference messagesRef;
    private token acceso = new token(this);
    private int idUsuario;
    private int idgrupo;
    private String image;
    private String nombreGrupo;
    private ImageButton btnAtras, btnEnviar;
    private TextView textviewNombreGrupo;
    private ImageView imgGrupo;
    private EditText mensaje;

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
        textviewNombreGrupo = findViewById(R.id.groupName);
        imgGrupo = findViewById(R.id.groupImage);
        mensaje = findViewById(R.id.messageField);

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

        messageAdapter = new MessageAdapter(messages, String.valueOf(idUsuario));
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

        imgGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityChat.this, ActivityGrupoInfo.class);
                intent.putExtra("idgrupo", idgrupo);
                startActivity(intent);
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}