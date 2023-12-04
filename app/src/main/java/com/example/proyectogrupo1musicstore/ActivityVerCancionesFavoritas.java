package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

public class ActivityVerCancionesFavoritas extends AppCompatActivity {
    ImageButton botonAtrass;

    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_canciones_favoritas);
        botonAtrass = (ImageButton) findViewById(R.id.btn_vertodoscancionesFavoritasListAtras);

        //progressDialog.show();
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        botonAtrass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}