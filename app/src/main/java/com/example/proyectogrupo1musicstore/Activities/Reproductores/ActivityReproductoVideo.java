package com.example.proyectogrupo1musicstore.Activities.Reproductores;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.proyectogrupo1musicstore.R;

public class ActivityReproductoVideo extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBarVideo;
    private Handler handler = new Handler();
    private Button btnPlay, btnPrevButton, btnForwardButton;
    private ImageButton btnCerrar;
    private String videoUrl, videoName;
    private TextView textViewVideoName, textViewPlay, textViewStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducto_video);

        videoUrl = getIntent().getStringExtra("videoUrl");
        videoName = getIntent().getStringExtra("name");

        videoView = findViewById(R.id.imagenreproductor);
        btnPlay = findViewById(R.id.playbtn);
        textViewVideoName = findViewById(R.id.txtMusicareproductor);
        seekBarVideo = findViewById(R.id.seekbavideor);
        textViewPlay = findViewById(R.id.txtPlay);
        textViewStop = findViewById(R.id.txtStop);
        btnCerrar = findViewById(R.id.imagebuttonCerrarVideo);
        btnPrevButton = findViewById(R.id.prevbtn);
        btnForwardButton = findViewById(R.id.stobbtn);

        textViewVideoName.setText(videoName);

        // Configura el MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Coloca el URI
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Set up the MediaPlayer
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Configura el seekbar
                setupSeekBar();
                // Start video playback
                btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.start();
                    }
                });
                btnForwardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.stopPlayback();
                        finish();
                    }
                });
                btnPrevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.pause();
                    }
                });
                // Actualizar seekbar
                updateSeekBar();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //Metodo para configurar el seekbar
    private void setupSeekBar() {
        // Configura el valor maximo
        seekBarVideo.setMax(videoView.getDuration());
        textViewStop.setText(String.valueOf(formatTime(videoView.getDuration())));

        // Listener
        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Actualiza el playback
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pause video playback when the user starts dragging the SeekBar
                videoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume video playback when the user stops dragging the SeekBar
                videoView.start();
            }
        });
    }

    // Método para actualizar seekbar y tiempo restante
    private void updateSeekBar() {
        Runnable updateSeekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int currentPosition = videoView.getCurrentPosition();
                    int totalDuration = videoView.getDuration();

                    // Update SeekBar progress
                    seekBarVideo.setProgress(currentPosition);

                    // Update TextView with time left
                    int timeLeft = totalDuration - currentPosition;
                    textViewPlay.setText(formatTime(timeLeft));
                }

                handler.postDelayed(this, 100);
            }
        };

        // Run the initial update
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