package com.example.proyectogrupo1musicstore;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class activity_login extends AppCompatActivity {

    /*Declaración de variables*/
    EditText txtLoginUsuario, txtLoginPassword;

    TextView txtviewOlvidaPassword;
    Button btnLoginEntrar, btnLoginRegistrarse;
    String contraseniaParaClave = "programacionMovil1";
    Boolean estadoLogin;

    CheckBox recordarmeInicioSesion;
    public static boolean estadoSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Amarrando las variables con el elemento de la interfaz*/
        btnLoginEntrar = (Button) findViewById(R.id.btnLoginEntrar);
        btnLoginRegistrarse = (Button) findViewById(R.id.btnRegistrarCrear);

        txtLoginUsuario = (EditText) findViewById(R.id.txtRegistrarUsuario);
        txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);
        txtviewOlvidaPassword = (TextView) findViewById(R.id.txtviewOlvidaPassword);
        recordarmeInicioSesion = (CheckBox) findViewById(R.id.checkLoginRecordar);



        /*Evento para el botón de entrar al sistema*/
        btnLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //la función validar permite validar cuando los campos están vacios y lanza una alerta
                if (validar() == true) {
                    //código de validar si el usuario y el password es correcto o no y dejarlo acceder a la pantalla de funcionalidades.
                    try {
                        validarUsuarioPassword();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    /*Aquí debe de mostrarse el mensaje personalizado*/
                    mensajesPersonalizadas();
                }
            }
        });


        /*Evento para el botón de ir al formulario de registrarse*/
        btnLoginRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrate = new Intent(getApplicationContext(), activity_registrarse.class);
                startActivity(registrate);
            }
        });

        /*Evento para mandar a llamar la ventana de recuperar contraseña*/
        txtviewOlvidaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recuperarContra = new Intent(getApplicationContext(), activity_recuperar_contrasena1.class);
                startActivity(recuperarContra);
            }
        });
        expresiones_regulares();
    }

    /*Metódo para validar credenciales de autenticación*/
    private void validarUsuarioPassword() throws Exception {
        //Obtener el estado del checkbox de recordarme
        final String encriptarUser = encriptarUsuario(txtLoginUsuario.getText().toString(), contraseniaParaClave);

        String url = "https://phpclusters-152621-0.cloudclusters.net/busquedaAutenticacion.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Crear una solicitud de objeto JSON
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Respuesta", response.toString());

                        try {
                            // Convertir la respuesta String en un JSONObject
                            JSONObject jsonObject = new JSONObject(response);


                            /*Parte donde se obtiene si el usuario existe o no*/
                            String estadoJson = jsonObject.getString("error");

                            if(estadoJson == "false"){ //si el usuario existe
                                /*Obtener el hash de la contra correcto para ese usuario que esta intentando loguiarse*/
                                String password = jsonObject.getString("contrasenia");
                                Log.d("La contraseña: ", password);
                                /*Comparar este hash contra el pass que el usuario digita en texto plano*/
                                if((permitirLogin(txtLoginPassword.getText().toString(),password) == true)){

                                    String textoLogin = "¡Estamos encantados de tenerte de vuelta!. Por favor, dirígete a la pantalla principal para explorar todas las funcionalidades de nuestra aplicación";
                                    activity_personalizado_confirmacion_correcta dialogFragment = activity_personalizado_confirmacion_correcta.newInstance(textoLogin);
                                    dialogFragment.show(getSupportFragmentManager(), "acceso");

                                    /*Obteniendo el token que se genera cuando el usuario se loguea*/
                                    String token = jsonObject.getString("token");
                                    /*Metodo para guardar ese token en el keystore*/
                                    //guardarToken();
                                    com.example.proyectogrupo1musicstore.Utilidades.Token.token acceso = new token(getApplicationContext());
                                    acceso.guardarTokenToKeystore(token);
                                    //acceso.mostrarToken();
                                    //acceso.recuperarTokenFromKeystore();

                                    // Crear un Handler para posponer la ejecución del código
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            /*ESTO SOLO ES DE PRUEBA*/
                                            if(recordarmeInicioSesion.isChecked() == true){
                                                estadoSeleccionado = true;
                                            }else{
                                                estadoSeleccionado = false;
                                            }
                                            /*Guardar ese valor en el sharedPreference para poder recuperarlo en caso de que la aplicación se cierre*/
                                            // Para guardar un valor
                                            SharedPreferences sharedPref = getSharedPreferences("estadoCheck", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("estadoCheck", estadoSeleccionado);
                                            editor.apply();
                                            //Log.d("Estado del checkbox pantalla 1",""+estadoSeleccionado);
                                            Intent intent = new Intent(getApplicationContext(), ActivityPantallaPrincipal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 5000); // 8 segundos


                                }else {
                                    /*Mandar a llamar ventana personalizada para decir credenciales inválidas*/
                                    String textoLogin = "¡Credenciales inválidas!";
                                    activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoLogin);
                                    dialogFragment.show(getSupportFragmentManager(), "advertencia");
                                }
                            }else{//si el usuario no existe.
                                /*Mandar a llamar ventana personalizada para recomendar crear una cuenta*/
                                String textoLogin = "¡Vaya! Parece que todavía no tienes una cuenta con nosotros. Pulsa en el botón de registrarse para crear una. ¡Es rápido y fácil!";
                                activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoLogin);
                                dialogFragment.show(getSupportFragmentManager(), "advertencia");
                            }
                            /*****/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Manejar la excepción si el string no es un JSON válido o si las claves no existen
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("encrypted_user", encriptarUser.trim()); // Envías los datos encriptados como parámetro
                //params.put("encrypted_password", encriptarPass.trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    /*Encriptar la contraseña y password antes de mandarlo al PHP con Advance Encryption Standar*/
    public static String encriptarUsuario(String usuario, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key); // aquí es donde se utiliza la clave para desencriptar, esta clave viene del metódod de abajo
        byte[] encVal = c.doFinal(usuario.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }


    /*Generar una clave secreta para ser utilizada para encriptar y desencriptar los datos con el algoritmo AES*/
    private static SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }


    public boolean validar(){
        boolean retorna = true;
        if(txtLoginUsuario.getText().toString().isEmpty()){
            retorna = false;
        }
        if(txtLoginPassword.getText().toString().isEmpty()){
            retorna = false;
        }
        return retorna;
    }

    public void mensajesPersonalizadas(){
        if(txtLoginUsuario.getText().toString().isEmpty()){
            String textoAdvertencia = "No se permite este campo vacío. Por favor, ingresa tu usuario para autenticarse.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(txtLoginPassword.getText().toString().isEmpty()){
            String textoAdvertencia = "No se permite este campo vacío. Por favor, ingresa tu contraseña para autenticarse.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
    }

    /*Metodo para verificar si la contraseña en texto plano coincide con el hash específico de la BD*/
    public boolean permitirLogin(String passwordPlano, String hash){
        /*Verificar la contraseña*/
        boolean doesMatch = BCrypt.checkpw(passwordPlano, hash);
        if (doesMatch) {
            Log.d("BCrypt", "La contraseña es correcta");
            estadoLogin = true;
        } else {
            Log.d("BCrypt", "La contraseña es incorrecta");
            estadoLogin = false;
        }
        return estadoLogin;
    }

    public void expresiones_regulares() {

        InputFilter filtroContraseniaLogin = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String permitidos = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0-9!@#$%^*()_-+={}[]|\\:;,.<>?/\"~";
                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) ||
                            permitidos.indexOf(currentChar) != -1)) {
                        mostrarMensajeError("Los espacios y los caracteres (&) y (') no estan permitidos");
                        return "";
                    }
                    if (Character.isWhitespace(currentChar)) {
                        mostrarMensajeError("Los espacios no están permitidos.");
                        return "";
                    }
                }
                return null; // Accept the original value
            }
        };

        InputFilter filtroUsuarioLogin = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String permitidos = "a-z0-9._";

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (!(Character.isLowerCase(currentChar) || Character.isDigit(currentChar) ||
                            permitidos.indexOf(currentChar) != -1)) {
                        mostrarMensajeError("Los nombres de usuario solo pueden contener letras del alfabeto en minúsculas (a - z), números, guiones bajos y puntos.");
                        return "";
                    }
                    if (Character.isWhitespace(currentChar)) {
                        mostrarMensajeError("No puede utilizar espacios.");
                        return "";
                    }
                }
                return null; // Accept the original value
            }
        };
        txtLoginUsuario.setFilters(new InputFilter[]{filtroUsuarioLogin});
        txtLoginPassword.setFilters(new InputFilter[]{filtroContraseniaLogin});
    }

    private void mostrarMensajeError(String mensaje) {
        // Aquí puedes implementar la lógica para mostrar el mensaje de error, por ejemplo, usando un TextView o un Toast.
        // Ejemplo con Toast:
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}