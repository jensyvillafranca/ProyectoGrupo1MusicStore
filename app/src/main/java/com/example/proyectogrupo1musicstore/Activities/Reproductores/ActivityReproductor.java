package com.example.proyectogrupo1musicstore.Activities.Reproductores;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.proyectogrupo1musicstore.Models.infoReproductor;
import com.example.proyectogrupo1musicstore.NetworkTaksMulti.infoAudioAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.IOException;
import java.util.List;

public class ActivityReproductor extends AppCompatActivity{
    private MediaPlayer musicaview;
    TextView nombreCanciones;
    ImageView imagebuttonEditarFotoAudio;
    String url;
    private Button btnPlays, btnPrevButtons, btnForwardButtons;
    Button btnStop,btnReproducirSiguiente,btnReproAtras;
    SeekBar seekMusick;
    private SimpleExoPlayer exoPlayer;

    private Handler handler = new Handler();

//modificado por JM
    ProgressDialog progressDialog;
    byte[] imgPerfilAudioByteArrays;
    private infoReproductor reproductoinfo;
     private TextView textViewMusicaName, textViewPlay, textViewStop;
    private SeekBar seekBarMusica;
    static MediaPlayer mediaPlayer;
    private PlayerView playerView;
    private ImageButton btnCerrar;
    private String musicaUrl, musicaName,imagen;
    private com.example.proyectogrupo1musicstore.Utilidades.Token.token token = new token(this);



    //Listo para extraer informacion de la base de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

       // idaudio = getIntent().getIntExtra("idaudio", 0);

        imagebuttonEditarFotoAudio = (ImageView) findViewById(R.id.imagenreproductors);
        nombreCanciones = (TextView) findViewById(R.id.txtMusicareproductors);
     //   imagen = getIntent().getStringExtra("imagen");
        musicaUrl = getIntent().getStringExtra("musicaUrl");
        musicaName = getIntent().getStringExtra("name");

        btnReproducirSiguiente = (Button) findViewById(R.id.stobbtn);
        btnReproAtras = (Button) findViewById(R.id.prevbtn);
        seekMusick = (SeekBar) findViewById(R.id.seekbars);
        btnPlays = findViewById(R.id.playbtns);
        btnPrevButtons = findViewById(R.id.prevbtnss);
        btnForwardButtons = findViewById(R.id.stobbtnsss);
        btnCerrar = findViewById(R.id.imagebuttonCerrarMusicas);
        textViewMusicaName = findViewById(R.id.txtMusicareproductors);
        textViewPlay = findViewById(R.id.txtPlays);
        textViewStop = findViewById(R.id.txtStops);

        textViewMusicaName.setText(musicaName);


        // Configura el MediaController
        MediaItem mediaItem = MediaItem.fromUri(musicaUrl);

        mediaPlayer = new MediaPlayer();
        Uri musicaUri = Uri.parse(musicaUrl);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), musicaUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Configura el SeekBar
                setupSeekBar();

                // Inicia la reproducción cuando se presiona el botón
                btnPlays.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.start();
                    }
                });

                // Detiene la reproducción y finaliza la actividad al presionar el botón de avance
                btnForwardButtons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.stop();
                        finish();
                    }
                });

                // Pausa la reproducción al presionar el botón de retroceso
                btnPrevButtons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.pause();
                    }
                });



                // Actualizar SeekBar y tiempo restante
                updateSeekBar();
            }
        });

        //metodo del Seebark
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });


    }
    private void setupSeekBar() {
        // Configura el valor máximo del SeekBar
        seekMusick.setMax(mediaPlayer.getDuration());
        textViewStop.setText(String.valueOf(formatTime(mediaPlayer.getDuration())));

        // Listener del SeekBar
        seekMusick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Actualiza la posición de reproducción
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pausa la reproducción cuando el usuario comienza a arrastrar el SeekBar
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Reanuda la reproducción cuando el usuario deja de arrastrar el SeekBar
                mediaPlayer.start();
            }
        });
    }

    // Método para actualizar seekbar y tiempo restante

    private void updateSeekBar() {
        Runnable updateSeekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int totalDuration = mediaPlayer.getDuration();

                    // Actualiza el progreso del SeekBar
                    seekMusick.setProgress(currentPosition);

                    // Actualiza el TextView con el tiempo restante
                    int timeLeft = totalDuration - currentPosition;
                    textViewPlay.setText(formatTime(timeLeft));
                }

                handler.postDelayed(this, 100);
            }
        };

        // Ejecuta la actualización inicial
        handler.post(updateSeekBarRunnable);
    }

    // Método para formatear el tiempo en formato mm:ss
    private String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }














}