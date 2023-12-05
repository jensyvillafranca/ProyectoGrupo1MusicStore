package com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_SubirMusicaAsyncTask extends AsyncTask<String, Void, Void> {

    private final String autor;
    private final int idUsuario;
    private final byte [] enlaceAudio;

    private final String enlacePortada;

    private final String idPlayList;

    private final String idFavorito;
    private final String nombreCancion;
    private final String genero;
    private final String album;
    private final Context context;

    private ProgressDialog progressDialog;
    private boolean onProgressCalled = false;


    /*Constructor donde se inicializan los datos que contendra la tabla de audios*/

    public Activity_SubirMusicaAsyncTask(Context context, String autor, int idUsuario, byte [] enlaceAudio, String enlacePortada, String idPlayList, String idFavorito, String nombreCancion, String album, String genero) {
        this.autor = autor;
        this.idUsuario = idUsuario;
        this.enlaceAudio = enlaceAudio;
        this.enlacePortada = enlacePortada;
        this.idPlayList = idPlayList;
        this.nombreCancion = nombreCancion;
        this.genero = genero;
        this.album = album;
        this.idFavorito = idFavorito;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Subiendo Audio...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /*Donde mandamos los datos al php para realizar la correspondiente inserción a la tabla de audios*/
    @Override
    protected Void doInBackground(String... params) {
            try {
                onProgressCalled = false;
                URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/audio.php"); //Objeto URL con la dirección web del archivo php: audio.php
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //Abre una conexión URL
                urlConnection.setRequestMethod("POST"); //Método de solicitud para la conexión HTTP
                urlConnection.setRequestProperty("Content-Type", "application/json"); //Los datos se mandaran en formato JSON
                urlConnection.setDoOutput(true); //Permitiendo el envío de datos a través de la conexión

                // Prepare JSON data
                String jsonData = dataJSON();
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(jsonData.getBytes());
                out.flush();
                out.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("SubirMusicaAsyncTask", "Response Code: " + responseCode);

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("Error",""+e);
            }
            return null;
    }
    private String dataJSON() {
        try {
            String base64Audio = Base64.encodeToString(enlaceAudio, Base64.DEFAULT);
            JSONObject jsonData = new JSONObject();
            jsonData.put("autor", autor);
            jsonData.put("idUsuario", idUsuario);
            jsonData.put("enlaceAudio", base64Audio);
            jsonData.put("enlacePortada", enlacePortada);
            jsonData.put("idPlayList", idPlayList);
            jsonData.put("idFavorito", idFavorito);
            jsonData.put("nombreCancion", nombreCancion);
            jsonData.put("album", album);
            jsonData.put("genero", genero);
            return jsonData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            if(onProgressCalled==false){
                // Me lleva a otra actividad
                Toast.makeText(context, "¡El audio se ha subido con éxito!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


