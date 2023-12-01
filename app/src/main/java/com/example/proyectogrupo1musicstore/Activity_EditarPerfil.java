package com.example.proyectogrupo1musicstore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectogrupo1musicstore.Adapters.AppData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLDecoder;

public class Activity_EditarPerfil extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    int IdPersonal;
    private ImageView imgFoto, imgCambiarPerfil;
    private StorageReference storageRef;
    ImageView btnAtras;
    TextView txtviewCambiarContrasenia, txtNombre;
    ImageView imgContrasenia;
    CheckBox chePerfil;
    int idVisualizacion;

    private Uri uri; // Variable para almacenar la URL de la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Obtener la referencia al Storage de Firebase
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://proyectogrupo1musicstore.appspot.com/imagenesEditarPerfil");


        IdPersonal = getIntent().getIntExtra("IdPersonal", -1);
        btnAtras = (ImageView) findViewById(R.id.btn_EditarPerfilAtras);
        imgContrasenia=findViewById(R.id.imgContrasenia);
        txtviewCambiarContrasenia=findViewById(R.id.txtviewCambiarContrasenia);
        txtNombre=findViewById(R.id.txtNombre);
        imgCambiarPerfil = findViewById(R.id.imgcambiarPerfil);
        imgFoto = findViewById(R.id.imgFoto);
        chePerfil = findViewById(R.id.chePerfil);
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
                Intent intent = new Intent(Activity_EditarPerfil.this, Activity_PerfilPersonal.class);
                startActivity(intent);
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
                            Log.d("Nombre: ", nombreCompleto);




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
                        imgFoto.setImageBitmap(imageBitmap);

                        // Subir la imagen a Firebase Storage
                        uploadImageToFirebase(imageBitmap);
                    }
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null) {
                    // Opción de galería seleccionada
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        imgFoto.setImageBitmap(imageBitmap);

                        // Subir la imagen a Firebase Storage
                        uploadImageToFirebase(imageBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        // Crear una referencia única para la imagen
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        // Convertir el Bitmap a bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Subir la imagen a Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // La imagen se subió exitosamente
                Toast.makeText(Activity_EditarPerfil.this, "Foto subida con éxito", Toast.LENGTH_SHORT).show();

                // Obtener la URL de la imagen después de subirla a Firebase
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Guardar la URL de la imagen en las preferencias compartidas
                        subirImagen(downloadUri.toString());

                        // Asignar la URL a la variable de clase
                        uri = downloadUri;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores en la subida de la imagen
                Toast.makeText(Activity_EditarPerfil.this, "Error al subir la foto", Toast.LENGTH_SHORT).show();
            }
        });
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
                        // Redireccionar a la clase "Activity_Principal"
                        // Intent intent = new Intent(Activity_EditarPerfil.this, activity_principal_login.class);
                        //  startActivity(intent);
                        //  finish();  // Cerrar la actividad actual para que no se pueda volver atrás
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

}

