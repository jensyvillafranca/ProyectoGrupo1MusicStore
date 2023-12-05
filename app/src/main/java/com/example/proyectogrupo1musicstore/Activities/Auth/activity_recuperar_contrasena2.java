package com.example.proyectogrupo1musicstore.Activities.Auth;


import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_recuperar_contrasena1.email;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_recuperar_contrasena1.verificationCode;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
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
import com.example.proyectogrupo1musicstore.activity_personalizado_advertencia;
import com.example.proyectogrupo1musicstore.activity_personalizado_confirmacion_correcta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_recuperar_contrasena2 extends AppCompatActivity {

    /*Declaración de variables*/
    Button btnRecuperarVerificar;


    EditText txtRecuperarVerificar;


    TextView txtviewVerificarEnviarNuevamente, txtviewActivaLetras;
    private int segundos = 60; //segundos
    private TextView txtviewCronometro; //textview donde aparecera el cronometro
    public static String verificationCode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena2);

        /*Amarrando los valores de las variables al objeto de la interfaz*/
        btnRecuperarVerificar = (Button) findViewById(R.id.btnRecuperarVerificar);
        txtRecuperarVerificar = (EditText) findViewById(R.id.txtRecuperarVerificar);
        txtviewCronometro = (TextView) findViewById(R.id.txtviewCronometro_NuevaC);
        txtviewActivaLetras = (TextView) findViewById(R.id.txtviewActivaLetras);
        txtviewVerificarEnviarNuevamente = (TextView) findViewById(R.id.txtviewVerificarEnviarNuevamente);
        txtviewCronometro = (TextView) findViewById(R.id.txtviewCronometro_NuevaC);
        txtviewActivaLetras = (TextView) findViewById(R.id.txtviewActivaLetras);
        //Log.d("Mensaje",email);
        btnRecuperarVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prueba = txtRecuperarVerificar.getText().toString();

                if (prueba.trim().equals(verificationCode2) || prueba.trim().equals(verificationCode) ) {
                    Intent contrasenaNueva = new Intent(getApplicationContext(), activity_recuperar_contrasena3.class);
                    startActivity(contrasenaNueva);
                } else {
                    Toast.makeText(getApplicationContext(), "Código no válido", Toast.LENGTH_LONG).show();
                }
            }
        });
        txtviewVerificarEnviarNuevamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Llamado al método para el reenvio del código*/
                reenviarCodigoVerificacion();
                segundos = 60;
            }
        });


        InputFilter codigoVerificacion = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Utiliza la expresión regular para filtrar la entrada
                if (source != null && !source.toString().matches("^[0-9]+$")) {
                    return "";
                }
                return null;
            }
        };
        txtRecuperarVerificar.setFilters(new InputFilter[]{codigoVerificacion});
    }



    public void mensajesPersonalizadas() {
        if (txtRecuperarVerificar.getText().toString().isEmpty()) {
            String textoAdvertencia = "No se permite este campo vacío. Por favor, ingresa tu código de confirmación.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if (txtRecuperarVerificar.getText().toString().length() < 6 || txtRecuperarVerificar.getText().toString().length() > 6) {
            String textoAdvertencia = "El código de confirmación debe constar de seis dígitos exactos; por favor, asegúrese de que el código proporcionado esté completo.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
    }


    public void reenviarCodigoVerificacion() {
        Log.d("Correo desde la otra ventana",email);
        tiempoCodigo();
        String url = "https://phpclusters-156700-0.cloudclusters.net/verificarCorreo_CambioPass.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String textoConfirmacion = "¡Código reenviado con exito! Por favor revisa tu correo electrónico!";
                        activity_personalizado_confirmacion_correcta dialogFragment = activity_personalizado_confirmacion_correcta.newInstance(textoConfirmacion);
                        dialogFragment.show(getSupportFragmentManager(), "confirmacion");

                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);

                            //Asignando a las variables status, message y verificationCode los valores que vienen del PHP
                            //String status = jsonObject.getString("status");
                            //String message = jsonObject.getString("message");
                            verificationCode2 = jsonObject.getString("verification_code");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("email",email);
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }


    /*Método para poner tiempo para reenvio del código*/
    public void tiempoCodigo() {
        //Log.d("Entra donde queremos","Entra donde queremos");
        /*Mostrar el conómetro*/
        txtviewCronometro.setVisibility(View.VISIBLE);

        /*Para acceder al recurso string*/
        txtviewActivaLetras.setText(R.string.tituloConometro);

        /*Desabilitar la opción de solicitar un nuevo código*/
        txtviewVerificarEnviarNuevamente.setEnabled(false);

        /*Para cambiar el color al textview de "Enviar de nuevo"*/
        txtviewVerificarEnviarNuevamente.setTextColor(Color.GRAY);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = segundos / 60;
                int secs = segundos % 60;

                String time = String.format("%02d:%02d", minutes, secs);
                txtviewCronometro.setText(time);

                if (segundos > 0) {
                    segundos--;
                    handler.postDelayed(this, 1000);
                }
                if (segundos == 0) { //Habilitar la opción de mandar otro código una vez terminado el tiempo o cuándo este llega a 0
                    txtviewVerificarEnviarNuevamente.setEnabled(true);

                    /*Para limpiar el mensaje que de tiempo de espera*/
                    txtviewActivaLetras.setText("");

                    /*Para ocultar nuevamente el cronometro*/
                    txtviewCronometro.setVisibility(View.INVISIBLE);

                    /*Pasar a color blanco el textview de "Enviar de nuevo"*/
                    txtviewVerificarEnviarNuevamente.setTextColor(Color.WHITE);
                }
            }
        });

    }
}