package com.example.proyectogrupo1musicstore.Activities.Auth;

import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_recuperar_contrasena1.email;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_login;
import com.example.proyectogrupo1musicstore.R;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class activity_recuperar_contrasena3 extends AppCompatActivity {

    Button btnRecuperarRestaurar;
    EditText txtRecuperarNuevoPass, txtRecuperarConfirmar;

    String contraseniaParaClave = "programacionMovil1"; // Clave para encriptar el correo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena3);

        btnRecuperarRestaurar = findViewById(R.id.btnRecuperarRestaurar);
        txtRecuperarNuevoPass = findViewById(R.id.txtRecuperarNuevoPass);
        txtRecuperarConfirmar = findViewById(R.id.txtRecuperarConfirmar);

        btnRecuperarRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtRecuperarNuevoPass.getText().toString().equals(txtRecuperarConfirmar.getText().toString())) {
                    try {
                        actualizarContrasenia();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    txtRecuperarNuevoPass.setText("");
                    txtRecuperarConfirmar.setText("");
                }
            }
        });
    }

    public void actualizarContrasenia() {
        String url = "https://phpclusters-156700-0.cloudclusters.net/actualizarContrasena.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest actualizarContraseniaRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "¡Credenciales actualizadas!", Toast.LENGTH_LONG).show();
                        // Aquí puedes manejar la respuesta del servidor, verificar si la contraseña se actualizó correctamente, etc.
                        // Si la actualización fue exitosa, entonces puedes iniciar sesión nuevamente
                        Intent login = new Intent(getApplicationContext(), activity_login.class);
                        startActivity(login);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al actualizar la contraseña: " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Este es el error",error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("correo", email.trim());
                parametros.put("contrasenia", encriptarPassword(txtRecuperarConfirmar.getText().toString()));
                return parametros;
            }
        };

        queue.add(actualizarContraseniaRequest);
    }

    /*Encriptar todas las contraseñas*/
    public static String encriptarPassword(String passwordPlana) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(passwordPlana, salt);
    }

}
