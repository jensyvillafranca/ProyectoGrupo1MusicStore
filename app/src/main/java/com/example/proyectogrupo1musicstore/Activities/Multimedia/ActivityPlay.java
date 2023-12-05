package com.example.proyectogrupo1musicstore.Activities.Multimedia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.widget.TextView;

import com.example.proyectogrupo1musicstore.NetworkTaksMulti.CreatePlayListTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.activity_personalizado_advertencia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ActivityPlay extends AppCompatActivity {
    ImageButton botonAtrass;
    EditText textNombrePlayList,txtBiografia;
    ImageView imgAgragarImagen, insertarInfo,ImageViewFoto;
    TextView agrearModificarPlayList;
    Button btnCance;
    List<Integer> selectedUserIds;
    byte[] imgPerfilByteArray;
    ProgressDialog progressDialog;
    private int idUsuario;
    private int idFavorito;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));

        selectedUserIds = getIntent().getIntegerArrayListExtra("selectedUserIds");

        imgAgragarImagen = (ImageView) findViewById(R.id.ImageViewFoto);
        botonAtrass = (ImageButton) findViewById(R.id.btnAtrass);
        insertarInfo = (ImageView) findViewById(R.id.ImageViewSubirArchi);
        textNombrePlayList = (EditText) findViewById(R.id.txtNombrePlayList);
        txtBiografia = (EditText) findViewById(R.id.txtBiografia);
        btnCance = (Button) findViewById(R.id.btnCancelar);
        agrearModificarPlayList = (TextView) findViewById(R.id.modificarPlayList);

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;

                if (view.getId() == R.id.btnAtrass) {
                    actividad = ActivityPlayList.class;
                }
                if (view.getId() == R.id.modificarPlayList) {
                    actividad = ActivityModificarPlayList.class;
                }

                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };
        botonAtrass.setOnClickListener(buttonClick);

        btnCance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });



        imgAgragarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActivityPlay.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(ActivityPlay.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                } else {
                    // Create an intent to pick an image from the gallery
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            }
        });



        //Insertar Datos
        insertarInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (validar() == true){
                    new CreatePlayListTask(ActivityPlay.this, selectedUserIds, textNombrePlayList.getText().toString(), txtBiografia.getText().toString(), imgPerfilByteArray, textNombrePlayList,idFavorito,idUsuario).execute();
                    setTitle("El nombre de la PlayList se agrego correctamente");
                }else{
                    mensajesPersonalizados();
                }

            }

        });
        botonAtrass.setOnClickListener(buttonClick);
        agrearModificarPlayList.setOnClickListener(buttonClick);

        //Listener para manejar el cierre del teclado con el boton de enter
        textNombrePlayList.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //Listener para manejar el cierre del teclado con el boton de enter
        txtBiografia.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }
    private void cancelar() {
        // Intent para ir a la actividad principal de la app
        Intent intent = new Intent(this, ActivityPlayList.class);
        startActivity(intent);
        finish();
    }

    /*Validación para no dejar campos vacíos*/
    public boolean validar(){
        boolean retorna = true;

        if(txtBiografia.getText().toString().isEmpty()){
            retorna = false;
        }
        if(textNombrePlayList.getText().toString().isEmpty()){
            retorna = false;
        }

        if(txtBiografia.getText().toString().isEmpty() && textNombrePlayList.getText().toString().isEmpty()){
            retorna = false;
        }
        return retorna;
    }

    public void mensajesPersonalizados(){

        if (imgAgragarImagen.getDrawable() == null) {
            String textoAdvertencia = "Por favor, selecciona una imagen.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }

        if(textNombrePlayList.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, rellena el campo de nombres de la PlayList.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }
        if(txtBiografia.getText().toString().isEmpty()){
            String textoAdvertencia = "Por favor, rellena el campo de biografia.";
            activity_personalizado_advertencia dialogFragment = activity_personalizado_advertencia.newInstance(textoAdvertencia);
            dialogFragment.show(getSupportFragmentManager(), "advertencia");
        }


    }




    // Método para cambiar a otra actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);

    }

    //override al metodo para procesar la seleccion de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Actualiza el imageview para colocar la imagen seleccionada
            ImageView imageView = findViewById(R.id.ImageViewFoto);
            imageView.setImageURI(selectedImageUri);

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                imgPerfilByteArray = getBytes(inputStream);
                // La imagen ahora se encuentra en forma de byte array dentro de la variable imgPerfilByteArray para poder guardarse en la base de datos
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                showPermissionExplanation();
            }
        }
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

    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permiso Requerido");
        builder.setMessage("Para acceder a tu galería y seleccionar una imagen, necesitamos el permiso de almacenamiento. Por favor, otorga el permiso en la configuración de la aplicación..");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the app settings so the user can grant the permission
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the user's decision to cancel the action
                // You can close the app or take other actions here
            }
        });
        builder.show();
    }
}