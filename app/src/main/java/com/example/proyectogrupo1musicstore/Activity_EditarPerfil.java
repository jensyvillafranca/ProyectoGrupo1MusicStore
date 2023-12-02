package com.example.proyectogrupo1musicstore;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import androidx.biometric.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Activity_EditarPerfil extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    int IdPersonal;
    private ImageView imgFoto, imgCambiarPerfil;
    private StorageReference storageRef;
    ImageView btnAtras;
    TextView txtviewCambiarContrasenia, txtNombre, txtviewHabilitarHuella;
    ImageView imgContrasenia;
    private token acceso = new token(this);
    CheckBox chePerfil, checkHabilitarHuella;
    int idVisualizacion;
    Boolean estadoComprobacion = false;
    String estadoCheck;

    private Uri uri; // Variable para almacenar la URL de la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Obtener la referencia al Storage de Firebase
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://proyectogrupo1musicstore.appspot.com/imagenesEditarPerfil");
        IdPersonal = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));
        btnAtras = (ImageView) findViewById(R.id.btn_EditarPerfilAtras);
        imgContrasenia=findViewById(R.id.imgContrasenia);
        txtviewCambiarContrasenia=findViewById(R.id.txtviewCambiarContrasenia);
        txtNombre=findViewById(R.id.txtNombre);
        imgCambiarPerfil = findViewById(R.id.imgcambiarPerfil);
        imgFoto = findViewById(R.id.imgFoto);
        chePerfil = findViewById(R.id.chePerfil);
        checkHabilitarHuella = findViewById(R.id.cheHuella);
        txtviewHabilitarHuella = findViewById(R.id.textviewHabilitarHuella);
        /*Cargar de forma automáticamente el estado del checkbox desde la base de datos*/
        cargarEstadoCheckbox();


        /*En caso de que el usuario tenga sensor de huella, hacer visible el check para habiliar/deshabilitar la huella*/
        if (detectarSensor()) {
            txtviewHabilitarHuella.setVisibility(View.VISIBLE);
            checkHabilitarHuella.setVisibility(View.VISIBLE);

            //Mandar a llamar método que verifica si el usuario tiene una huella confugurada en el dispositivo
            //Pero hacerlo por medio de un evento en el checkbox
            verificarCheck_Huella();
        } else {
            /*En caso de que el usuario no tenga sensor de huella, dejar invisible el check para habiliar/deshabilitar la huella*/
            txtviewHabilitarHuella.setVisibility(View.INVISIBLE);
            checkHabilitarHuella.setVisibility(View.INVISIBLE);
        }

        imgCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar los permisos de la cámara
                if (ContextCompat.checkSelfPermission(Activity_EditarPerfil.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_EditarPerfil.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    showOptionsDialog();
                }
            }
        });


        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_CambiarContrasena.class);
                startActivity(intent);
            }
        });

        txtviewCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_CambiarContrasena.class);
                startActivity(intent);
            }
        });

        String url = "https://phpclusters-152621-0.cloudclusters.net/mostrarUsuario.php?id="+IdPersonal+"";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombreCompleto = response.getString("nombres") + " " + response.getString("apellidos");
                            String imageUrl = response.getString("enlacefoto");
                            idVisualizacion = response.getInt("idvisualizacion");
                            chePerfil.setChecked(idVisualizacion == 0);
                            try {
                                // Parsear la URL
                                URL url = new URL(imageUrl);

                                // Obtener el protocolo y el host
                                String protocol = url.getProtocol();
                                String host = url.getHost();

                                // Verificar si la URL comienza con la cadena específica
                                String prefijo = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil/";
                                if (imageUrl.startsWith(prefijo)) {
                                    // Obtener la ruta completa y el query
                                    String pathAndQuery = url.getPath() + "?" + url.getQuery();

                                    // Decodificar la ruta y el query
                                    String decodedPathAndQuery = URLDecoder.decode(pathAndQuery, "UTF-8");

                                    // Dividir la URL en dos partes
                                    String primeraParte = "https://firebasestorage.googleapis.com/v0/b/proyectogrupo1musicstore.appspot.com/o/imagenesEditarPerfil%2F";
                                    String segundaParte = url.getFile().substring(url.getPath().lastIndexOf('/') + 1);

                                    imageUrl = primeraParte + segundaParte;
                                    Log.d("Estado URL", "url incorrecta: "+imageUrl);

                                } else {
                                    Log.d("Estado URL", "url correcta: "+imageUrl);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                            Glide.with(Activity_EditarPerfil.this)
                                    .load(imageUrl)
                                    .into(imgFoto);
                            txtNombre.setText(nombreCompleto);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        // Llamada al método mensajes
        mensajes(Activity_EditarPerfil.this);
        chePerfil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int nuevoIdVisualizacion = isChecked ? 0 : 1;
                Log.d("Visualizacion: ", String.valueOf(nuevoIdVisualizacion));
                editarVisualizacion(nuevoIdVisualizacion);
            }
        });

        /*Habilitar/Deshabilitar huella biométrica*/
        checkHabilitarHuella.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar una opción")
                .setItems(new CharSequence[]{"Cámara", "Galería"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Opción de cámara seleccionada
                            dispatchTakePictureIntent();
                        } else {
                            // Opción de galería seleccionada
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, REQUEST_PICK_IMAGE);
                        }
                    }
                });
        builder.create().show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, mostrar el diálogo de opciones
                showOptionsDialog();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        // Convertir la imagen a Base64
                        String base64Image = encodeToBase64(imageBitmap);

                        // Subir la imagen a Firebase Storage
                        uploadImageToFirebase(base64Image);
                    }
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null) {
                    // Opción de galería seleccionada
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        // Convertir la imagen a Base64
                        String base64Image = encodeToBase64(imageBitmap);

                        // Subir la imagen a Firebase Storage
                        subirImagen(base64Image);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    // Método para convertir una imagen Bitmap a Base64
    private String encodeToBase64(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    // Eliminar Cuenta --------------------------------------------------------------------
    public void mensajes(Context context) {
        ImageView imgCuenta = findViewById(R.id.imgCuenta);
        imgCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("¿Eliminar Cuenta?");
                builder.setMessage("Su cuenta se eliminará de manera permanente");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Es lo que se va a ejecutar
                        // Aquí puedes poner el código para eliminar la cuenta
                        eliminarCuenta();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No debería realizar ninguna Acción, ya que la respuesta es No
                    }
                });
                builder.show();
            }
        });
    }

    private void subirImagen(String url){

        String urlSubirImagenes;
        urlSubirImagenes = "https://phpclusters-152621-0.cloudclusters.net/editarFotoPerfil.php/?id="+IdPersonal+"&enlaceFoto="+url;
        JSONObject postData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlSubirImagenes, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void eliminarCuenta(){

        String urlEliminarCuenta = "https://phpclusters-152621-0.cloudclusters.net/eliminarCuenta.php/?id="+IdPersonal;
        JSONObject postData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlEliminarCuenta, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void editarVisualizacion (int idVisualizacion){

        String urlEditarVisualizacion = "https://phpclusters-152621-0.cloudclusters.net/editarVisualizacion.php/?id="+IdPersonal+"&idvisualizacion="+idVisualizacion;
        JSONObject postData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlEditarVisualizacion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    //Verificar si el celular tiene sensor de huella digital
    private boolean detectarSensor() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6 (Marshmallow) hasta Android 9, usar FingerprintManager
            FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
            return fingerprintManager != null && fingerprintManager.isHardwareDetected();
        }
        return false;
    }

    //Verificar si el dispositivo tiene una huella configurada
    public void verificarExistenciaHuella(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Log.d("No hay huellas","No hay huellas");
                    //Mostrar una advertencia al usuario
                    activity_personalizada_huella dialogFragment = activity_personalizada_huella.newInstance("");
                    dialogFragment.show(getSupportFragmentManager(), "advertencia");
                    checkHabilitarHuella.setChecked(false);
                } else {
                    Log.d("Hay huellas","Hay huellas");
                    //Verificar esa huella que existe en el dispositivo, es decir que sea la misma huella de la persona que esta usando la app.
                    //Para ello mando a llamar el metodo de showBiometricPrompt
                    if(estadoComprobacion==false){
                        showBiometricPrompt();
                    }
                }
            }
        }
    }

    //Metodo para ver el estado del checkbox y asi habilitar/deshabilitar el acceso con la huella
    public void verificarCheck_Huella(){
        checkHabilitarHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estadoComprobacion ==true) //si esta en true
                {
                    //Aqui puedo deshabilitar el servicio
                    checkHabilitarHuella.setChecked(false);
                    estadoComprobacion = false;
                    //Aqui vamos a mandar a llamar el update del campo estado de la tabla de usuarios dependiendo del valor del checkbox.
                    mandarValor();
                }else{
                    //Si doy click y no hay huellas me lanza la advertencia
                    //Si doy click y si hay huellas entonces le pido que la verifique
                    //Finalmente dejo seleccionado el checkbox
                    verificarExistenciaHuella();
                    //Al cargar nuevamente la pantalla de editar perfil, ese chechbox debe reflejar el estado en 0 u 1 del usuario
                    //Al insertar el usuario ese campo de autenticacion debera estar en 0 por defecto indicando que inicialmente el usuario no tiene acceso a este tipo de autenticación
                }
            }
        });
    }

    public void mandarValor(){
        String url = "https://phpclusters-152621-0.cloudclusters.net/actualizarAcceso.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_LONG).show();
                        Log.d("Respuesta del servidor campo acceso: ",""+response.toString());
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
                Log.d("Estado del checkbox",""+checkHabilitarHuella.isChecked());
                parametros.put("acceso", String.valueOf(checkHabilitarHuella.isChecked()));
                parametros.put("idUsuario", String.valueOf(IdPersonal));
                return parametros;
            }
        };
        queue.add(resultadoPost);
    }

    public void showBiometricPrompt() {
        FragmentActivity activity = this; // tu actividad
        Executor executor = ContextCompat.getMainExecutor(activity);
        BiometricPrompt biometricPrompt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    // Manejar error, puede ser que el usuario canceló, etc.
                    //checkHabilitarHuella.setChecked(false);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    // Autenticación exitosa, proceder con la acción después de la verificación
                    Log.d("Verificacion exitosa","Verificacion exitosa");
                    checkHabilitarHuella.setChecked(true);
                    estadoComprobacion=true;
                    //Aqui vamos a mandar a llamar el update del campo estado de la tabla de usuarios dependiendo del valor del checkbox.
                    mandarValor();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    // Manejar el caso de que la autenticación falle (la huella no coincide)
                    Log.d("Verificacion no exitosa","Verificacion no exitosa");
                   // checkHabilitarHuella.setChecked(false);
                }
            });
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verificación de huella dactilar")
                .setSubtitle("Coloca tu dedo en el sensor para verificar tu identidad")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }


    /*Cargar el estado del chebox por medio del campo usuario*/
    public void cargarEstadoCheckbox(){
        String url = "https://phpclusters-152621-0.cloudclusters.net/estadoCheckbox.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest resultadoPost = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Convertir la respuesta en un objeto JSON
                            JSONObject jsonObject = new JSONObject(response);
                            //Asignar el valor que viene del PHP del estado del checkbox a la variable estado
                            estadoCheck = jsonObject.getString("acceso");
                            if(estadoCheck.equals("t")){
                                //Poner el checkbox marcado
                                checkHabilitarHuella.setChecked(true);
                                estadoComprobacion=true;

                            }else{
                                //Poner el checkbox desmarcado
                                checkHabilitarHuella.setChecked(false);
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
                parametros.put("idUsuario", String.valueOf(IdPersonal));
                return parametros;
            }
        };
        queue.add(resultadoPost);

    }

}

