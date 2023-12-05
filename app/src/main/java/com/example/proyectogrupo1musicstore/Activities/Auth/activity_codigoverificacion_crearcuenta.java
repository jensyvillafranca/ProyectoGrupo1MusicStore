package com.example.proyectogrupo1musicstore.Activities.Auth;


import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.form_apellidos;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.form_correo;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.form_nombres;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.form_password;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.form_usuario;
import static com.example.proyectogrupo1musicstore.Activities.Auth.activity_registrarse.verificationCode;

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
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;


public class activity_codigoverificacion_crearcuenta extends AppCompatActivity {

    /*Declaración de variables*/
    Button btnRecuperarRegistrarse;

    EditText txtRecuperarRegistrarse;

    TextView txtviewVerificarEnviarNuevamente,txtviewActivaLetras;
    String token = "null"; /*Para mensajes push*/
    String enlaceFoto = "https://storage.googleapis.com/proyectogrupo1musicstore.appspot.com/imagenesPerfil/avatar.png";
    int idVisualizacion = 1;

    int idEstado = 1;


    /*Variables para el tiempo de reenvio del código*/
    private int segundos = 60; //segundos
    private TextView txtviewCronometro; //textview donde aparecera el cronometro



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigoverificacion_crearcuenta);

        /*Amarrando los valores de las variables al objeto de la interfaz*/
        btnRecuperarRegistrarse = (Button) findViewById(R.id.btnRecuperarRegistrarse);
        txtRecuperarRegistrarse = (EditText) findViewById(R.id.txtRecuperarRegistrarse);
        txtviewVerificarEnviarNuevamente = (TextView) findViewById(R.id.txtviewVerificarEnviarNuevamente);
        txtviewCronometro = (TextView) findViewById(R.id.txtviewCronometro);
        txtviewActivaLetras = (TextView) findViewById(R.id.txtviewActivaLetras);


        /*Evento para el botón que deja acceder a la pantalla principal de la aplicación de acuerdo al código de verificación correcto*/
        btnRecuperarRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //la función validar permite validar cuando los campos están vacios y lanza una alerta
                if(validar() == true){
                    /*Aquí debe de ir el llamado a la ventana donde se encuentra el menú desplegable*/
                    insertarUsuario();
                }else{
                    /*Ventana personalizada*/
                    mensajesPersonalizadas();
                }
            }
        });

        /*Evento para poder reenviar el código de verificación*/
        txtviewVerificarEnviarNuevamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Llamado al método para el reenvio del código*/
                segundos = 60;
                reenviarCodigoVerificacion();
            }
        });

        /*Expresión regular para que ingrese únicamente números en el textview de código*/
        InputFilter codigoVerificacion = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Utiliza la expresión regular para filtrar la entrada
                if (source != null && !source.toString().matches("^[0-9]+$")) {
                    return "";
                }
                return null;
            }
        };
        txtRecuperarRegistrarse.setFilters(new InputFilter[]{codigoVerificacion});
    }

    public boolean validar(){
        boolean retorna = true;
        if(txtRecuperarRegistrarse.getText().toString().isEmpty()){
            retorna = false;
        }
        if(txtRecuperarRegistrarse.getText().toString().length() < 6 || txtRecuperarRegistrarse.getText().toString().length() > 6){
            retorna = false;
        }
        return retorna;
    }


    /*Enviar mensajes a la ventana personalizada*/
    public void mensajesPersonalizadas(){
        if(txtRecuperarRegistrarse.getText().toString().isEmpty()){
            String textoAdvertencia = "No se permite este campo vacío. Por favor, ingresa tu código de confirmación.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(txtRecuperarRegistrarse.getText().toString().length() < 6 || txtRecuperarRegistrarse.getText().toString().length() > 6){
            String textoAdvertencia = "El código de confirmación debe constar de seis dígitos exactos; por favor, asegúrese de que el código proporcionado esté completo.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
    }



    /*Método para reenviar el código en caso de que el primero enviado ya este inactivo*/
    public void reenviarCodigoVerificacion() {
        //Log.d("Correo desde la otra ventana",form_correo);
        tiempoCodigo();
        String url = "https://phpclusters-156700-0.cloudclusters.net/verificacionCorreo.php";
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
                            verificationCode = jsonObject.getString("verification_code");

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
                parametros.put("email", form_correo);
                return parametros;
            }
        };

        queue.add(resultadoPost);
    }


    /*Método para poner tiempo para reenvio del código*/
    public void tiempoCodigo(){
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
                if(segundos == 0){ //Habilitar la opción de mandar otro código una vez terminado el tiempo o cuándo este llega a 0
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


    /*Metódo para insertar usuario en la base de datos, luego de verificar el código*/
    public void insertarUsuario() {
        /*Comparar que el código que el usuario ingresa, es el mismo del correo electrónico*/
        String prueba = txtRecuperarRegistrarse.getText().toString();
        Log.d("El del usuario", prueba);
        Log.d("El código", verificationCode);


        if(prueba.trim().equals(verificationCode)){

            /*Mostrar mensaje de verificación completada*/
            /*Insertar el usuario en la base de datos.*/

            String url = "https://phpclusters-156700-0.cloudclusters.net/insertarUsuario.php";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest resultadoPost = new StringRequest(
                    Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String textoConfirmacion = "¡Tu cuenta ha sido creada exitosamente!";
                            activity_personalizado_confirmacion_correcta dialogFragment = activity_personalizado_confirmacion_correcta.newInstance(textoConfirmacion);
                            dialogFragment.show(getSupportFragmentManager(), "confirmacion");


                            // Crear un Handler para posponer la ejecución del código
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Este código se ejecutará después del tiempo de retraso
                                    Intent intent = new Intent(getApplicationContext(), activity_principal_login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 5000); // 8 segundos
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
                    parametros.put("nombres", form_nombres);
                    parametros.put("apellidos", form_apellidos);
                    parametros.put("correo", form_correo);
                    parametros.put("usuario", form_usuario);
                    parametros.put("contrasenia", encriptarPassword(form_password));
                    parametros.put("token", token.toString());
                    parametros.put("enlacefoto", enlaceFoto.toString());
                    parametros.put("idvisualizacion", Integer.toString(idVisualizacion));
                    parametros.put("idestado", Integer.toString(idEstado));
                    return parametros;
                }
            };
            queue.add(resultadoPost);
        }else{
            Toast.makeText(getApplicationContext(), "Código no válido", Toast.LENGTH_LONG).show();
        }
    }

    /*Encriptar todas las contraseñas*/
    public static String encriptarPassword(String passwordPlana) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(passwordPlana, salt);
    }
}