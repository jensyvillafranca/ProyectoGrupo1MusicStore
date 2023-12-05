package com.example.proyectogrupo1musicstore.Activities.Auth;
import static com.example.proyectogrupo1musicstore.Activities.Auth.Activity_CambiarContrasena.correo_usuario;
import static com.example.proyectogrupo1musicstore.Activities.Auth.Activity_CambiarContrasena.nuevoPass_Encriptada;
import static com.example.proyectogrupo1musicstore.Activities.Auth.Activity_CambiarContrasena.verificationCode_NuevaContra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.activity_personalizado_advertencia;
import com.example.proyectogrupo1musicstore.activity_personalizado_confirmacion_correcta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_ConfirmaCambioContrasena extends AppCompatActivity {

    ImageView btnAtras;
    Button btnVerificar;

    TextView inputVerificacion, txtviewEnviarCodigoNuevamente, txtviewCronometro_NuevaC, txtviewActivaLetras_Nueva;
    private int segundos = 60; //segundos
    private token acceso = new token(this);
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_cambio_contrasena);

        btnAtras = (ImageView) findViewById(R.id.btn_ConfirmarCorreo_Atras);
        btnVerificar = (Button) findViewById(R.id.btn_Verificar);
        inputVerificacion = (TextView) findViewById(R.id.inputVerificacion);
        txtviewCronometro_NuevaC = (TextView) findViewById(R.id.txtviewCronometro_NuevaC);
        txtviewActivaLetras_Nueva = (TextView) findViewById(R.id.txtviewActivaLetras_Nueva);
        txtviewEnviarCodigoNuevamente = (TextView) findViewById(R.id.txtviewEnviarCodigoNuevamente);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegresarCambioContrasena = new Intent(Activity_ConfirmaCambioContrasena.this, Activity_CambiarContrasena.class);
                startActivity(RegresarCambioContrasena);
            }
        });

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar() == true){
                    /*Verificar que el código ingresado por el usuario sea el mismo que el que viene del PHP*/
                    actualizarContrasenia();
                }else{
                    /*Ventana personalizada*/
                    mensajesPersonalizados();
                }
            }
        });

        txtviewEnviarCodigoNuevamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Llamado al método para el reenvio del código*/
                segundos = 60;
                reenviarCodigoVerificacion();
            }
        });
    }

    public boolean validar(){
        boolean retorna = true;
        if(inputVerificacion.getText().toString().isEmpty()){
            retorna = false;
        }
        return retorna;
    }

    public void mensajesPersonalizados(){

        if(inputVerificacion.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, rellena el campo de código de verificación";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
    }

    /*Método para reenviar el código en caso de que el primero enviado ya este inactivo*/
    public void reenviarCodigoVerificacion() {
        //Log.d("Correo desde la otra ventana",form_correo);
        tiempoCodigo();
        String url = "https://phpclusters-156700-0.cloudclusters.net/verificarCorreo_CambioPass.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*Para mandar a llamar la ventana modal para decirle al usuario que el código se ha reenviado*/
                        String textoConfirmacion = "¡Código reenviado con exito! Por favor revisa tu correo electrónico!";
                        activity_personalizado_confirmacion_correcta dialogFragment = activity_personalizado_confirmacion_correcta.newInstance(textoConfirmacion);
                        dialogFragment.show(getSupportFragmentManager(), "confirmacion");

                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);

                            //Asignando a las variables status, message y verificationCode los valores que vienen del PHP
                            //String status = jsonObject.getString("status");
                            //String message = jsonObject.getString("message");
                            verificationCode_NuevaContra = jsonObject.getString("verification_code");

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
                parametros.put("email", correo_usuario);
                return parametros;
            }
        };

        queue.add(resultadoPost);
    }


    /*Método para poner tiempo para reenvio del código*/
    public void tiempoCodigo(){
        /*Mostrar el conómetro*/
        txtviewCronometro_NuevaC.setVisibility(View.VISIBLE);

        /*Para acceder al recurso string*/
        txtviewActivaLetras_Nueva.setText(R.string.tituloConometro);

        /*Desabilitar la opción de solicitar un nuevo código*/
        txtviewEnviarCodigoNuevamente.setEnabled(false);

        /*Para cambiar el color al textview de "Enviar de nuevo"*/
        txtviewEnviarCodigoNuevamente.setTextColor(Color.GRAY);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = segundos / 60;
                int secs = segundos % 60;

                String time = String.format("%02d:%02d", minutes, secs);
                txtviewCronometro_NuevaC.setText(time);

                if (segundos > 0) {
                    segundos--;
                    handler.postDelayed(this, 1000);
                }
                if(segundos == 0){ //Habilitar la opción de mandar otro código una vez terminado el tiempo o cuándo este llega a 0
                    txtviewEnviarCodigoNuevamente.setEnabled(true);

                    /*Para limpiar el mensaje que de tiempo de espera*/
                    txtviewActivaLetras_Nueva.setText("");

                    /*Para ocultar nuevamente el cronometro*/
                    txtviewCronometro_NuevaC.setVisibility(View.INVISIBLE);

                    /*Pasar a color blanco el textview de "Enviar de nuevo"*/
                    txtviewEnviarCodigoNuevamente.setTextColor(Color.WHITE);
                }
            }
        });
    }

    public void actualizarContrasenia() {
        if(inputVerificacion.getText().toString().trim().equals(verificationCode_NuevaContra)){
            String url = "https://phpclusters-156700-0.cloudclusters.net/actualizarContrasenia_NuevaContra.php";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest actualizarContraseniaRequest = new StringRequest(
                    Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Respuesta", response);
                            if(response.toString().equals("La contraseña del usuario se actualizó correctamente")){
                                String textoConfirmacion = "¡Credencial actualizada correctamente!";
                                activity_personalizado_confirmacion_correcta dialogFragment = activity_personalizado_confirmacion_correcta.newInstance(textoConfirmacion);
                                dialogFragment.show(getSupportFragmentManager(), "confirmacion");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent login = new Intent(getApplicationContext(), activity_login.class);
                                        startActivity(login);
                                    }
                                }, 5000); // 8 segundos
                            }else{
                                Toast.makeText(getApplicationContext(), "No se pudo actualizar la contraseña", Toast.LENGTH_LONG).show();
                            }
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
                    parametros.put("idUsuario", String.valueOf(idUsuario));
                    parametros.put("contrasenia", nuevoPass_Encriptada);
                    return parametros;
                }
            };

            queue.add(actualizarContraseniaRequest);
        }else{
            Toast.makeText(getApplicationContext(), "Código no válido", Toast.LENGTH_LONG).show();
        }

    }

}