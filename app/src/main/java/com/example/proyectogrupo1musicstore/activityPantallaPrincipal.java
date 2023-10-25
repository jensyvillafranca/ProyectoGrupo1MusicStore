package com.example.proyectogrupo1musicstore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class activityPantallaPrincipal extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton openMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        // Inicialización de las vistas y elementos del diseño
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openMenuButton = (ImageButton) findViewById(R.id.btn_PrincipalDesplegable);

        // Listener para abrir el menú lateral
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });
    }
}