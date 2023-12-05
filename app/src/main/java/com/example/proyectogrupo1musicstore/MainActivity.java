package com.example.proyectogrupo1musicstore;





import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_principal_login;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private ImageView gifImageView;
    private ProgressBar progressBar;

    private token acceso;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //iniciando firebase
        FirebaseApp.initializeApp(this);

        acceso = new token(this);

        gifImageView = findViewById(R.id.gifImageView);
        progressBar = findViewById(R.id.progressBar);



        // Cargar el GIF con Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.cargando)
                .into(gifImageView);

        // Simular una carga
        simulateLoading();

        progressBar.setVisibility(View.INVISIBLE);

    }



    private void simulateLoading() {
        // Simular un proceso de carga (por ejemplo, 3 segundos)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ocultar el GIF y el ProgressBar
                gifImageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                /*Obtener el valor que viene del sharedPreferences, seria el estado del chechbox*/
                SharedPreferences sharedPref = getSharedPreferences("estadoCheck", Context.MODE_PRIVATE);
                Boolean valorCheck = sharedPref.getBoolean("estadoCheck", false);

                if (usuarioEstaLogueado() && valorCheck == true) {
                    irAPantallaPrincipal();
                } else {
                    irAPantallaLogin();
                }
            }
        }, 5000); // 5000 milisegundos (5 segundos)
    }

    private boolean usuarioEstaLogueado() {
        String token = acceso.recuperarTokenFromKeystore();
        return token != null && !token.isEmpty();
    }

    private void irAPantallaPrincipal() {
        // Intent para ir a la actividad principal de la app
        Intent intent = new Intent(this, ActivityPantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    private void irAPantallaLogin() {
        // Intent para ir a la actividad de inicio de sesión
        Intent intent = new Intent(this, activity_principal_login.class);
        startActivity(intent);
        finish();
    }
}