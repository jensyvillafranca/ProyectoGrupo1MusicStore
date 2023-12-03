package com.example.proyectogrupo1musicstore;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.proyectogrupo1musicstore.Models.infoReproductor;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.infoAudioAsyncTask;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class ActivityReproductor extends AppCompatActivity implements infoAudioAsyncTask.DataFetchListener{
    private int idaudio;
    private int idUsuario;
    TextView nombreCanciones;
    ImageView imagebuttonEditarFotoAudio;
    String url;

    Button btnPlay, btnStop,btnReproducirSiguiente,btnReproAtras;
    SeekBar seekMusick;
    private SimpleExoPlayer exoPlayer;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

//modificado por JM
    ProgressDialog progressDialog;
    byte[] imgPerfilAudioByteArrays;
    private infoReproductor reproductoinfo;

    static MediaPlayer mediaPlayer;
    private PlayerView playerView;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);

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
        nombreCanciones = (TextView) findViewById(R.id.txtMusicareproductors);
        btnPlay = (Button) findViewById(R.id.playbtns);
        btnReproducirSiguiente = (Button) findViewById(R.id.stobbtn);
        btnReproAtras = (Button) findViewById(R.id.prevbtn);
        seekMusick = (SeekBar) findViewById(R.id.seekbars);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }




        // = getIntent().getIntExtra("idUsuario", 0);

        // Obtiene la informacion del servidor
        String url = "https://phpclusters-152621-0.cloudclusters.net/obtenerAudioReproductor.php";
        //progressDialog.show();
        new infoAudioAsyncTask(this).execute(url, String.valueOf(idUsuario));










    }

    @Override
    public void onPlayClick(String audioUrl) {
        // Use the mainHandler to ensure UI operations are on the main thread
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("AudioUrl", audioUrl);
                initializePlayer();
                MediaItem mediaItem = MediaItem.fromUri(audioUrl);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                exoPlayer.setPlayWhenReady(true);
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initializePlayer(){
        if (exoPlayer == null) {
            // Create a SimpleExoPlayer instance
            exoPlayer = new SimpleExoPlayer.Builder(this).build();

            // Bind the player to the view
            playerView = findViewById(R.id.playbtns);
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(exoPlayer);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDataFetched(List<infoReproductor> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            reproductoinfo = dataList.get(0);


            imagebuttonEditarFotoAudio.setImageBitmap(reproductoinfo.getImage());
            nombreCanciones.setText(reproductoinfo.getnombre());
          //  seekbars.OnSeekBarChangeListener(reproductoinfo.getUrl());


        } else {
            Log.e("Error", "No data fetched from the server");
        }

    }






}