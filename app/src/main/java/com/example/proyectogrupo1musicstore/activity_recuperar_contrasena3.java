package com.example.proyectogrupo1musicstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.proyectogrupo1musicstore.R;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class activity_recuperar_contrasena3 extends AppCompatActivity {

    Button btnRecuperarRestaurar;
    EditText txtRecuperarNuevoPass, txtRecuperarConfirmar;

    // Asegúrate de inicializar estas variables adecuadamente
    String email = ""; // Debes obtener el email de algún lugar
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
        final String encriptarCorreo = encriptarCorreo(email, contraseniaParaClave);

        String url = "https://phpclusters-152621-0.cloudclusters.net/actualizarContrasenia.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest actualizarContraseniaRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Respuesta", response);
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
                parametros.put("contrasenia", txtRecuperarConfirmar.getText().toString());
                Log.d("El mensaje que quiero",encriptarCorreo);
                Log.d("El mensaje que quiero",encriptarCorreo.trim());
                parametros.put("correo", encriptarCorreo.trim());
                return parametros;
            }
        };

        queue.add(actualizarContraseniaRequest);
    }

    // Métodos de encriptación
    public static String encriptarCorreo(String correo, String password) {
        try {
            SecretKeySpec key = generateKey(password);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(correo.getBytes());
            return Base64.encodeToString(encVal, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }
}
