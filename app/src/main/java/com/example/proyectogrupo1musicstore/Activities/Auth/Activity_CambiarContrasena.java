package com.example.proyectogrupo1musicstore.Activities.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectogrupo1musicstore.Activities.Perfil.Activity_EditarPerfil;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.activity_personalizado_advertencia;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class Activity_CambiarContrasena extends AppCompatActivity {
    /*Declaración de variables*/
    ImageView btnCambiarAtras;
    Button btnEnviarCodigo, btnActualizarUsuario;

    EditText inputContraActual, inputContraNueva, inputConfirmarContrasena;

    /*Variables globales para ir haciendo el update de la contra*/
    public static String nuevoPass_Encriptada, verificationCode_NuevaContra, correo_usuario;
    String nuevoPass, actualPassUsuario, actualPassBd;
    int idUsuario;
    private token acceso = new token(this);
    Boolean estadoContraActual;
    TextView errorMensaje, errorMensaje2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        inputContraActual = findViewById(R.id.inputContrasenaActual);
        inputContraNueva = findViewById(R.id.inputNuevaContasena);
        inputConfirmarContrasena = findViewById(R.id.inputConfirmaContrasena);
        btnActualizarUsuario = findViewById(R.id.btnEnviar);
        btnCambiarAtras = (ImageView) findViewById(R.id.btn_CambiarContrasena_Atras);
        btnEnviarCodigo = (Button) findViewById(R.id.btnEnviar);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        errorMensaje = (TextView) findViewById(R.id.errorMensaje);
        errorMensaje2 = (TextView) findViewById(R.id.errorMensaje2);

        btnCambiarAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegresarCambiarContrasena= new Intent(getApplicationContext(), Activity_EditarPerfil.class);
                startActivity(RegresarCambiarContrasena);
            }
        });


        btnEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar() == true){
                    //Verificar que la contraseña que el usuario esta ingresando como actual es la que esta en la BD
                    verificarPasswordActual();
                }else{
                    /*Ventana personalizada*/
                    mensajesPersonalizados();
                }
            }
        });
        /*Llamada al método de validar expresiones regulares*/
        expresiones_regulares();
    }
    // Validar campos vacios
    public boolean validar(){
        boolean retorna = true;

        if(inputContraActual.getText().toString().isEmpty()){
            retorna = false;
        }
        if(inputContraNueva.getText().toString().isEmpty()){
            retorna = false;
        }
        if(inputConfirmarContrasena.getText().toString().isEmpty()){
            retorna = false;
        }
        if(!inputContraNueva.getText().toString().equals(inputConfirmarContrasena.getText().toString())){
            retorna = false;
        }
        if(inputContraActual.getText().toString().equals(inputContraNueva.getText().toString())){
            retorna = false;
        }
        return retorna;
    }
    //Mostrar mensajes personalizados
    public void mensajesPersonalizados(){
        errorMensaje2.setText("");
        errorMensaje.setText("");
        if(inputContraActual.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, rellena el campo con tu contraseña actual.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(inputContraNueva.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, rellena el campo con tu nueva contraseña.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(inputConfirmarContrasena.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, confirma tu contraseña.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(!inputContraNueva.getText().toString().equals(inputConfirmarContrasena.getText().toString())){
            String textoAdvertencia = "La nueva contraseña no cooncide con la contraseña verificada, revise nuevamente";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(inputContraActual.getText().toString().equals(inputContraNueva.getText().toString())){
            errorMensaje.setText("");
            errorMensaje2.setText("Contraseña igual a la actual");
        }
    }

    public void verificarcorreo() {
        String url = "https://phpclusters-156700-0.cloudclusters.net/buscarCorreo_NuevaContra.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest verificacionRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*Obtener el correo que viene desde el PHP*/
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            correo_usuario = jsonObject.getString("correo");
                            Log.d("Aqui viene el correo",correo_usuario);
                            /*Mandar ese correo al metodo de enviar el código*/
                            enviarCodigoVerificacion(correo_usuario);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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
                parametros.put("idUsuario", String.valueOf(idUsuario));
                return parametros;
            }
        };
        queue.add(verificacionRequest);
    }

    public void enviarCodigoVerificacion(String email_nuevaContra) {
        /*Asignando a esa variable global el valor que el usuario escribe*/
        nuevoPass = inputContraNueva.getText().toString();
        nuevoPass_Encriptada = encriptarPassword(nuevoPass);

        String url = "https://phpclusters-156700-0.cloudclusters.net/verificarCorreo_CambioPass.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Código mandado", Toast.LENGTH_LONG).show();

                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);
                            //Obteniendo el código que viene desde el PHP
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
                parametros.put("email", email_nuevaContra);
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    /*Encriptar todas las contraseñas*/
    public static String encriptarPassword(String passwordPlana) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(passwordPlana, salt);
    }

    public void expresiones_regulares(){
        InputFilter usuarioContrasenia = new InputFilter() {
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
        // Aplicar el filtro al EditText
        // Reemplaza 'R.id.miEditText' con el ID de tu EditText

        inputContraActual.setFilters(new InputFilter[]{usuarioContrasenia});
        inputConfirmarContrasena.setFilters(new InputFilter[]{usuarioContrasenia});
        inputContraNueva.setFilters(new InputFilter[]{usuarioContrasenia});
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }


    //Verificar que la contraseña que el usuario esta ingresando como la actual es la que viene de la BD
    public void verificarPasswordActual(){
        /*Obteniendo el valor que el usuario ingresa como su contraseña actual*/
        actualPassUsuario = inputContraActual.getText().toString();

        String url = "https://phpclusters-156700-0.cloudclusters.net/contraActual.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), "Código mandado", Toast.LENGTH_LONG).show();

                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);
                            //Obteniendo el código que viene desde el PHP
                            actualPassBd = jsonObject.getString("contrasenia");
                            if((permitirLogin(actualPassUsuario,actualPassBd))==true){ //las contraseñas son iguales se puede proceder

                                //Validar que la nueva contraseña que el usuario ha ingresado no sea igual que la antigua
                                if(inputContraActual.getText().toString().equals(inputContraNueva.getText().toString())){
                                    errorMensaje.setText("");
                                    errorMensaje2.setText("Contraseña igual a la actual");
                                }else{
                                    errorMensaje2.setText("");
                                    Log.d("Estado de la contra", ""+estadoContraActual);
                                    errorMensaje.setText("");
                                    errorMensaje2.setText("");
                                    //Verificar el correo de ese usuario al que se le va mandar el código
                                    verificarcorreo();
                                    Intent verificarCorreo = new Intent(getApplicationContext(), Activity_ConfirmaCambioContrasena.class);
                                    startActivity(verificarCorreo);
                                }
                            }else{
                                errorMensaje2.setText("");
                                errorMensaje.setText("Contraseña Actual Incorrecta");
                            }
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
                parametros.put("idUsuario", String.valueOf(idUsuario));
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    /*Metodo para verificar si la contraseña en texto plano coincide con el hash específico de la BD para la contraseña actual de usuario*/
    public boolean permitirLogin(String passwordPlano, String hash){
        /*Verificar la contraseña*/
        boolean doesMatch = BCrypt.checkpw(passwordPlano, hash);
        if (doesMatch) {
            Log.d("BCrypt", "Las contraseñas coonciden");
            estadoContraActual = true;
        } else {
            Log.d("BCrypt", "Las contraseñas no coonciden");
            estadoContraActual = false;
        }
        return estadoContraActual;
    }
}