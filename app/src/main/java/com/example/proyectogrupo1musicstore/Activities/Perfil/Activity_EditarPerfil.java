package com.example.proyectogrupo1musicstore.Activities.Perfil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import androidx.biometric.BiometricPrompt;

import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.example.proyectogrupo1musicstore.Activities.Auth.Activity_CambiarContrasena;
import com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks.UpdateImagenPerfilAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.AppPreferences.AppPreferences;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_login;
import com.example.proyectogrupo1musicstore.Activities.Auth.activity_personalizada_huella;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    CheckBox chePerfil, checkHabilitarHuella, checkBoxNotifications;
    int idVisualizacion;
    Boolean estadoComprobacion = false;
    String estadoCheck;
    private String oldImageUrl;
    private Bitmap imgPerfilBitmap;
    private byte[] imgPerfilByteArray;
    private Integer notificationValue;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;
    private static final int peticion_acceso_camera = 101;
    static final int peticion_toma_fotografia = 102;

    private Uri uri; // Variable para almacenar la URL de la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Log.e("IdPersonal", String.valueOf(IdPersonal));

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
        checkBoxNotifications = findViewById(R.id.cheNotificacion);
        /*Cargar de forma automáticamente el estado del checkbox desde la base de datos*/
        cargarEstadoCheckbox();

        Log.e("IdPersonal", String.valueOf(IdPersonal));

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

        notificationValue = AppPreferences.getUserScore(this);
        checkBoxNotifications.setChecked(notificationValue == 1);

        // Listener para apagar o prender las notificaciones
        checkBoxNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationValue = 1;
                } else {
                    notificationValue = 0;
                }
                // Guarda en SharedPreferences
                AppPreferences.setUserScore(Activity_EditarPerfil.this, notificationValue);
            }
        });

        imgCambiarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
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

        String url = "https://phpclusters-156700-0.cloudclusters.net/mostrarUsuario.php?id="+IdPersonal+"";
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
                                oldImageUrl = prefijo;
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
                            permisos();
                        } else {
                            // Opción de galería seleccionada
                            checkPermissions();
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

        if (requestCode == peticion_acceso_camera) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                String content = "camara";
                showPermissionExplanation(content);
            }
        }
        if ((requestCode == REQUEST_CODE) || (requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickImage();
            } else {
                String content = "galería y seleccionar una imagen";
                showPermissionExplanation(content);
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == peticion_toma_fotografia && resultCode == RESULT_OK) {
            try {
                // Actualiza el imageview para colocar la imagen seleccionada
                RoundedImageView imageView = findViewById(R.id.imgFoto);

                Bundle extras = data.getExtras();
                imgPerfilBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imgPerfilBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgPerfilBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imgPerfilByteArray = stream.toByteArray();
                updateImage();

            }catch (Exception ex){
                ex.toString();
            }
        }
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();

                // Actualiza el imageview para colocar la imagen seleccionada
                RoundedImageView imageView = findViewById(R.id.imgFoto);
                imageView.setImageURI(selectedImageUri);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    imgPerfilByteArray = getBytes(inputStream);
                    updateImage();
                    // La imagen ahora se encuentra en forma de byte array dentro de la variable imgPerfilByteArray para poder guardarse en la base de datos
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    private void eliminarCuenta(){

        String urlEliminarCuenta = "https://phpclusters-156700-0.cloudclusters.net/eliminarCuenta.php/?id="+IdPersonal;
        JSONObject postData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlEliminarCuenta, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cerrarSesion();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void cerrarSesion() {
        acceso.borrarToken();
        AppPreferences.resetFirstTimePreferences(Activity_EditarPerfil.this);
        // Regresar al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(Activity_EditarPerfil.this, activity_login.class);
        startActivity(intent);
        finish();
    }

    private void editarVisualizacion (int idVisualizacion){

        String urlEditarVisualizacion = "https://phpclusters-156700-0.cloudclusters.net/editarVisualizacion.php/?id="+IdPersonal+"&idvisualizacion="+idVisualizacion;
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
        String url = "https://phpclusters-156700-0.cloudclusters.net/actualizarAcceso.php";
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
        String url = "https://phpclusters-156700-0.cloudclusters.net/estadoCheckbox.php";
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

    //Permisos Camara
    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, peticion_acceso_camera);
        } else {
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, peticion_toma_fotografia);
        }
    }

    //metodo para revisar los permisos
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(Activity_EditarPerfil.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Activity_EditarPerfil.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(Activity_EditarPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Activity_EditarPerfil.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        }
    }

    //Funcion para otorgar permisos manualmente
    private void showPermissionExplanation(String content) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu " + content + ", necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
        builder.setPositiveButton("Ir a Ajustes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abre los ajustes de la app para que el usuario pueda otorgar permiso
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Controlla decision negativa
            }
        });
        builder.show();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void updateImage() {

        //combierte la imagen a base64
        String base64Image = Base64.encodeToString(imgPerfilByteArray, Base64.DEFAULT);

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idusuario", IdPersonal);
            jsonData.put("imagen", base64Image);
            jsonData.put("urlanterior", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //llama el asynctask
        new UpdateImagenPerfilAsyncTask(Activity_EditarPerfil.this).execute(jsonData.toString());

        //guarda la imagen en sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences("image_bitmap_perfil", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_for_image", base64Image);
        editor.apply();
    }

}

