package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Models.infoEditarPlayList;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.EditarPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.infoPlayListEditarAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.UI.ConfirmationDialog;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ActivityEditarPlayList extends AppCompatActivity implements infoPlayListEditarAsyncTask.DataFetchListener{
    ImageView imagebuttonEditarFotoPlay;

    TextView textNombre, textBiogreafia;
    ProgressDialog progressDialog;
    ImageButton btnAtras;

    Button btnActualizar;
    private int idplaylist;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);
    private int idUsuario;
    private String urlAnterior;
    byte[] imgPerfilByteArrays;
    private infoEditarPlayList playlistInfo;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;
    private static final int REQUEST_CODE_EXTERNAL = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_play_list);

       // progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Cargando...");
        //progressDialog.setCancelable(false);

        idplaylist = getIntent().getIntExtra("idplaylist", 0);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));
        textNombre = (TextView) findViewById(R.id.txtNombrePlayListEditar);
        textBiogreafia = (TextView) findViewById(R.id.txtBiografiaEditar);
        imagebuttonEditarFotoPlay = (ImageView) findViewById(R.id.ImageViewFotoEditar);
        btnActualizar = (Button) findViewById(R.id.btnSubirArchiEditar);
        btnAtras = (ImageButton) findViewById(R.id.btnEditarAtrass);



        // Obtiene la informacion del servidor
        String url = "https://phpclusters-156700-0.cloudclusters.net/obtenerinfoEditarPlayList.php";
        //progressDialog.show();
        new infoPlayListEditarAsyncTask(this).execute(url, String.valueOf(idplaylist));

        imagebuttonEditarFotoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationDialog.showConfirmationDialog(
                        ActivityEditarPlayList.this, // Replace with your activity or fragment context
                        "Confirmación",
                        "¿Está seguro de que desea realizar estos cambios?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updatePlayList();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Si el usuario hace click en no
                                dialog.dismiss();
                            }
                        }
                );

            }
        });
    }

    @Override
    public void onDataFetched(List<infoEditarPlayList> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            playlistInfo = dataList.get(0);

            textNombre.setText(playlistInfo.getNombre());
            imagebuttonEditarFotoPlay.setImageBitmap(playlistInfo.getImage());
            textBiogreafia.setText(playlistInfo.getBiografia());
        } else {
            Log.e("Error", "No data fetched from the server");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Actualiza el imageview para colocar la imagen seleccionada
            ImageView imageView = findViewById(R.id.ImageViewFotoEditar);
            imageView.setImageURI(selectedImageUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                imgPerfilByteArrays = getBytes(inputStream);
                // La imagen ahora se encuentra en forma de byte array dentro de la variable imgPerfilByteArray para poder guardarse en la base de datos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == REQUEST_CODE)||(requestCode == REQUEST_CODE_EXTERNAL)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                pickImage();
            } else {
                showPermissionExplanation();
            }
        }
    }

    //metodo para revisar los permisos
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android 10 and above, request READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(ActivityEditarPlayList.this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityEditarPlayList.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        } else {
            // Android 9 and below, request WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(ActivityEditarPlayList.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityEditarPlayList.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL);
            } else {
                // Permission is granted, proceed to pick an image
                pickImage();
            }
        }
    }

    //metodo para escojer la imagen
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Funcion para otorgar permisos manualmente
    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu galería y seleccionar una imagen, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
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

    private void updatePlayList(){

        //obtener la imagen del imageview y convertirlo a byte array
        Drawable drawable = imagebuttonEditarFotoPlay.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imgPerfilByteArrays = stream.toByteArray();


        //combierte la imagen a base64
        String base64Image = Base64.encodeToString(imgPerfilByteArrays, Base64.DEFAULT);

        //obtiener el url de la imagen actual para poder ser eliminada
        urlAnterior = playlistInfo.getUrlAnterior();

        //crea el json
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idplaylist", idplaylist);
            jsonData.put("nombre", textNombre.getText().toString());
            jsonData.put("biografia", textBiogreafia.getText().toString());
            jsonData.put("imagen", base64Image);
            jsonData.put("urlanterior", urlAnterior);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //llama el asynctask
        new EditarPlayListAsyncTask(ActivityEditarPlayList.this).execute(jsonData.toString());
    }

}