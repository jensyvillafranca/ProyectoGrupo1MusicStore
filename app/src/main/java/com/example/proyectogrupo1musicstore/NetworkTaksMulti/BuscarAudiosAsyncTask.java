package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterBuscarGrupos;
import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterMusicaVideos;
import com.example.proyectogrupo1musicstore.Models.buscarAudioMusica;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
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

public class BuscarAudiosAsyncTask extends AsyncTask<String, Void, Pair<Integer, List<buscarAudioMusica>>> {
    private static final String TAG = "BuscarAudiosAsyncTask";
    private Context context;
    private RecyclerView recyclerView;
    private CustomAdapterMusicaVideos adapter;
    private ProgressDialog progressDialog;

    public BuscarAudiosAsyncTask(Context context, RecyclerView recyclerView, CustomAdapterMusicaVideos adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.progressDialog = progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Buscando...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected Pair<Integer, List<buscarAudioMusica>> doInBackground(String... params) {
        String idusuario = params[0];
        String search = params[1];

        try {
            URL url = new URL("https://phpclusters-152621-0.cloudclusters.net/busccarCanciones.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("nombrecancion", search);
            jsonRequest.put("idusuario", idusuario);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonRequest.toString().getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();

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

                List<buscarAudioMusica> result = parseJsonResponse(response.toString());
                return new Pair<>(responseCode, result);
            } else {
                Log.e(TAG, "Error response code: " + responseCode);
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(HttpURLConnection.HTTP_INTERNAL_ERROR, null);
    }

    @Override
    protected void onPostExecute(Pair<Integer, List<buscarAudioMusica>> result) {
        progressDialog.dismiss();

        int responseCode = result.first;
        List<buscarAudioMusica> data = result.second;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Parse and process the JSON data
            if (data != null) {
                adapter.setDataList(data);
                adapter.notifyDataSetChanged();
            }
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            // No groups found, show a toast
            Toast.makeText(context, "¡No se encontró ninguna Musical!", Toast.LENGTH_SHORT).show();
        } else {
            // Handle other response codes or errors
            Toast.makeText(context, "¡No se encontró ninguna musica!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<buscarAudioMusica> parseJsonResponse(String json) {
        List<buscarAudioMusica> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idaudio = jsonObject.getInt("idaudio");
                String nombre = jsonObject.getString("nombrecancion");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlaceportada"));
                String genero = jsonObject.getString("genero");

                dataList.add(new buscarAudioMusica(idaudio, nombre,imageResource, genero));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }
        return dataList;
    }
}
