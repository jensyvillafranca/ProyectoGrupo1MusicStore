package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.proyectogrupo1musicstore.Models.infoEditarPlayList;
import com.example.proyectogrupo1musicstore.Models.infoReproductor;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.EditarPlayListAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.infoAudioAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.infoPlayListEditarAsyncTask;
import com.example.proyectogrupo1musicstore.Utilidades.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ActivityReproductor extends AppCompatActivity implements infoAudioAsyncTask.DataFetchListener{
    private int idaudio;
    private int idUsuario;
    ImageView imagebuttonEditarFotoAudio;
    ProgressDialog progressDialog;
    byte[] imgPerfilAudioByteArrays;
    private infoReproductor reproductoinfo;
    private com.example.proyectogrupo1musicstore.Utilidades.token token = new token(this);

    //Listo para extraer informacion de la base de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        //idaudio = getIntent().getIntExtra("idaudio", 0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

       // idaudio = getIntent().getIntExtra("idaudio", 0);
        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(token.recuperarTokenFromKeystore()));
        imagebuttonEditarFotoAudio = (ImageView) findViewById(R.id.imagenreproductors);

        // = getIntent().getIntExtra("idUsuario", 0);

        // Obtiene la informacion del servidor
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerAudioReproductor.php";
        //progressDialog.show();
        new infoAudioAsyncTask(this).execute(url, String.valueOf(idUsuario));


    }

    @Override
    public void onDataFetched(List<infoReproductor> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            reproductoinfo = dataList.get(0);


            imagebuttonEditarFotoAudio.setImageBitmap(reproductoinfo.getImage());

        } else {
            Log.e("Error", "No data fetched from the server");
        }

    }


}