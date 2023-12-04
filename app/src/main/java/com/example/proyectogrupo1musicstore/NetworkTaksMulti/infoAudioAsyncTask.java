package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.example.proyectogrupo1musicstore.Models.infoReproductor;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class infoAudioAsyncTask extends AsyncTask<String, Void, List<infoReproductor>> {

    private static final String TAG = "infoAudioAsyncTask";
    private DataFetchListener dataFetchListener;
    public infoAudioAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }

    @Override
    protected List<infoReproductor> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idUsuario = params[1]; // idplaylist parametro

        try {
            // construye el URL
            URL url = new URL(urlString);
            Log.d(TAG, "URL: " + url.toString());

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idusuario", Integer.valueOf(idUsuario));

            // Escribe el JSON al output stream
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonParams.toString().getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            Log.d(TAG, "Response Code: " + responseCode + ", Message: " + responseMessage);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                inputStream.close();

                return parseJsonResponse(response.toString());
            } else {
                Log.e("infoAudioAsyncTask", "Error response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<infoReproductor> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }


    private List<infoReproductor> parseJsonResponse(String json) {
        List<infoReproductor> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
               //modificado
                // Extrae la informacion y crea objetos
                Integer idaudio = jsonObject.getInt("idaudio");
                String nombre = jsonObject.getString("nombrecancion");
                Integer idowner = jsonObject.getInt("idusuario");
                String url = jsonObject.getString("enlaceaudio");
                Log.d("Enlaceaudio",url);
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlaceportada"));

                dataList.add(new infoReproductor(idaudio, nombre, idowner,  url, imageResource));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    public interface DataFetchListener {
        void onPlayClick(String audioUrl);

        void onDataFetched(List<infoReproductor> dataList);
    }
}
