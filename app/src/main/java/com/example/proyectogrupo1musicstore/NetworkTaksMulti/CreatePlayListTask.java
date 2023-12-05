package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;


import com.example.proyectogrupo1musicstore.Activities.Multimedia.ActivityPlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CreatePlayListTask extends AsyncTask<String, Void, Void> {
    private ProgressDialog progressDialog;
    private Context context;
    private List<Integer> selectedUserIds;
    private String playName;
    private String biografiaDescripcion;

    private EditText textNombrePlayList;
    private byte[] playImage;
    private boolean onProgressCalled = false;
    private int idFavorito;
    private int idUsuario;

    public CreatePlayListTask(Context context, List<Integer> selectedUserIds, String playName, String biografiaDescripcion, byte[] playImage, EditText textNombrePlayList, int idFavorito, int idUsuario) {
        this.context = context;
        this.selectedUserIds = selectedUserIds;
        this.playName = playName;
        this.biografiaDescripcion = biografiaDescripcion;
        this.playImage = playImage;
        this.textNombrePlayList = textNombrePlayList;
        this.idFavorito = idFavorito;
        this.idUsuario = idUsuario;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Creating play...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected Void doInBackground(String... params) {
        try{
            onProgressCalled = false;
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/crearPlayLists.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Prepare JSON data
            String jsonData = prepareJsonData();
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonData.getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("CreatePlayListTask", "Response Code: " + responseCode);

            urlConnection.disconnect();


        } catch (Exception e) {
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
                Intent intent = new Intent(context, ActivityPlay.class);
                context.startActivity(intent);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        textNombrePlayList.setError("Nombre de la PlayList ya existe!");
        onProgressCalled = true;
    }

    private String prepareJsonData() {
        try {
            JSONArray jsonUserIds = new JSONArray(selectedUserIds);

            String base64Image = Base64.encodeToString(playImage, Base64.DEFAULT);

            JSONObject jsonData = new JSONObject();
            jsonData.put("nombre", playName);
            jsonData.put("biografia", biografiaDescripcion);
            jsonData.put("idtipoplaylist", 1); // Replace with the actual user ID
            jsonData.put("idfavorito", idFavorito); // Replace with the actual user ID
            jsonData.put("imagen", base64Image);
            jsonData.put("idusuario", idUsuario);

            return jsonData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkPlayExistence(String playName) {
        try {
            // Prepara el JSON
            JSONObject jsonData = new JSONObject();
            jsonData.put("nombre", playName);

            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/exitePlayList.php");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (InputStream is = connection.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getBoolean("success");
            }
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
