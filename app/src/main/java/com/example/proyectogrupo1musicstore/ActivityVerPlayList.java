package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityVerPlayList extends AppCompatActivity {
    ImageButton botonAtrass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_play_list);

        botonAtrass = (ImageButton) findViewById(R.id.btn_vertodosPlayListAtras);
        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;

                if (view.getId() == R.id.btn_vertodosPlayListAtras) {
                    actividad = ActivityPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        botonAtrass.setOnClickListener(buttonClick);
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);

    }

}