package com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia;
import android.app.ProgressDialog;
import android.content.Context;
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

public class Activity_SubirVideoAsyncTask extends AsyncTask<String, Void, Void> {

    private final String autor;
    private final int idUsuario;
    private final byte [] enlaceVideo;

    private final String enlacePortada;

    private final String idPlayList;

    private final String idFavorito;
    private final String nombreVideo;
    private final String genero;
    private final String duracion;
    private final String fechaLanzamiento;

    private final Context context;

    private ProgressDialog progressDialog;
    private boolean onProgressCalled = false;


    /*Constructor donde se inicializan los datos que contendra la tabla de videos*/
    public Activity_SubirVideoAsyncTask(Context context, String autor, int idUsuario, byte[] enlaceVideo, String enlacePortada, String idPlayList, String idFavorito, String nombreVideo, String genero, String duracion, String fechaLanzamiento) {
        this.autor = autor;
        this.idUsuario = idUsuario;
        this.enlaceVideo = enlaceVideo;
        this.enlacePortada = enlacePortada;
        this.idPlayList = idPlayList;
        this.idFavorito = idFavorito;
        this.nombreVideo = nombreVideo;
        this.genero = genero;
        this.duracion = duracion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Subiendo Video...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /*Donde mandamos los datos al php para realizar la correspondiente inserción a la tabla de videos*/
    @Override
    protected Void doInBackground(String... params) {
            try {
                onProgressCalled = false;
                URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/video.php"); //Objeto URL con la dirección web del archivo php: video.php
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
                Log.d("SubirVideoAsyncTask", "Response Code: " + responseCode);

                urlConnection.disconnect();
            } catch (Exception e) {
                Log.d("Error",""+e);
            }
            return null;
    }
    private String dataJSON() {
        try {
            String base64Video = Base64.encodeToString(enlaceVideo, Base64.DEFAULT);
            JSONObject jsonData = new JSONObject();
            jsonData.put("autor", autor);
            jsonData.put("idUsuario", idUsuario);
            jsonData.put("enlaceVideo", base64Video);
            jsonData.put("enlacePortada", enlacePortada);
            jsonData.put("idPlayList", idPlayList);
            jsonData.put("idFavorito", idFavorito);
            jsonData.put("nombreVideo", nombreVideo);
            jsonData.put("genero", genero);
            jsonData.put("duracion", duracion);
            jsonData.put("fechaLanzamiento", fechaLanzamiento);
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
                Toast.makeText(context, "¡El video se ha subido con éxito!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


