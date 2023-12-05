package com.example.proyectogrupo1musicstore.Activities.Auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_recuperar_contrasena1 extends AppCompatActivity {

    //Declaración de variables/
    Button btnRecuperarEnviar;

    TextView errorMensaje;
    EditText correo_electronico;
    public static String email;

    public static String verificationCode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena1);

        //Amarrando los valores de las variables al objeto de la interfaz/
                btnRecuperarEnviar = (Button) findViewById(R.id.btnRecuperarRegistrarse);
        correo_electronico = (EditText) findViewById(R.id.txtRecuperarRegistrarse);



        // Resto del código de la solicitud Volley...

        btnRecuperarEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    verificarcorreo();
                    // Verifica si el correo existe en la base de datos
                }
            }
        });

        expresiones_regulares();

    }


    private void mostrarMensajeError(String mensaje) {
        // Aquí puedes implementar la lógica para mostrar el mensaje de error, por ejemplo, usando un TextView o un Toast.
        // Ejemplo con Toast:
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }


    public void verificarcorreo() {
        String url = "https://phpclusters-156700-0.cloudclusters.net/confirmarcorreobd.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest verificacionRequest = new StringRequest(
                Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("El usuario con el correo existe en la base de datos.")) {
                            // El correo existe en la base de datos, aquí puedes realizar la lógica de enviar el código de verificación
                            email = correo_electronico.getText().toString();

                            Intent enviarCodigoVerificacion = new Intent(getApplicationContext(), activity_recuperar_contrasena2.class);
                            startActivity(enviarCodigoVerificacion);
                            enviarCodigoVerificacion();

                        } else if (response.equals("El usuario con el correo no existe en la base de datos.")) {
                            mostrarMensajeError("El correo no existe en la base de datos.");
                        } else {
                            // Manejar otro tipo de respuesta
                            mostrarMensajeError("Respuesta inesperada del servidor");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("correo", correo_electronico.getText().toString());
                return parametros;
            }
        };

        queue.add(verificacionRequest);
    }



    //Método para poder mandar el código de verificación/
    public boolean enviarCodigoVerificacion() {

        String url = "https://phpclusters-156700-0.cloudclusters.net/verificarCorreo_CambioPass.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Validar el correo electrónico
        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Codigo enviado", Toast.LENGTH_LONG).show();

                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);
                            verificationCode = jsonObject.getString("verification_code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("email", correo_electronico.getText().toString());
                return parametros;
            }
        };

        queue.add(resultadoPost);
        return false;
    }


    // Función para validar el formato del correo electrónico


    private void mostrarMensaje(String mensaje) {
        // Aquí puedes implementar la lógica para mostrar el mensaje de error, por ejemplo, usando un TextView o un Toast.
        // Ejemplo con Toast:
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }



    public boolean validar(){
        boolean retorna = true;
        if(correo_electronico.getText().toString().isEmpty()){
            correo_electronico.setError("Confirmación de correo vacío");
            retorna = false;
        }
        return retorna;


    }


    public void expresiones_regulares() {


        // Filtro para el correo electrónico
        InputFilter filtroCorreo1 = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String permitidos = "@._";

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) ||
                            permitidos.indexOf(currentChar) != -1)) {
                        mostrarMensajeError("Los caracteres que unicamente estan permitidos son (_)(@)(.) y las letras minúsculas del alfabeto (a - z).");
                        return "";
                    }
                    if (Character.isWhitespace(currentChar)) {
                        mostrarMensajeError("Los espacios no están permitidos.");
                        return "";
                    }
                }
                return null;
            }
        };


        correo_electronico.setFilters(new InputFilter[]{filtroCorreo1});

    }

}