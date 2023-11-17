package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityPlayList extends AppCompatActivity {
    Button CrearPlays;
    TextView txtSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);


        txtSiguiente = (TextView) findViewById(R.id.txtPrincipal);


        CrearPlays = (Button) findViewById(R.id.btnCrear);
        CrearPlays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creacion = new Intent(getApplicationContext(),Activity_CrearPlayList.class);
                startActivity(creacion);
            }
        });



        //Listener para manejar el cierre del teclado con el boton de enter
        txtSiguiente.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }


}