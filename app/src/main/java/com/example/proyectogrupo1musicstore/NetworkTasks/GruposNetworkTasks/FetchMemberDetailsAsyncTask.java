package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMemberDetailsAsyncTask extends AsyncTask<String, Void, List<vistaDeNuevoGrupo>> {

    private static final String TAG = "FetchMemberDetailsAsyncTask";
    private DataFetchListener dataFetchListener;
    private List<vistaDeNuevoGrupo> dataList;
    private int idUsuario;
    private int version;

    public FetchMemberDetailsAsyncTask(DataFetchListener listener, List<vistaDeNuevoGrupo> dataList, int version) {
        this.dataFetchListener = listener;
        this.dataList = dataList;
        this.version = version;
    }

    @Override
    protected List<vistaDeNuevoGrupo> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        idUsuario = Integer.parseInt(params[1]);

        try {
            // construye el URL
            URL url = new URL(urlString);

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idusuario", idUsuario);

            // Escribe el JSON al output stream
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonParams.toString().getBytes());
            out.flush();
            out.close();

            // Obtiene la respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();

            // Parse the JSON response
            return parseJsonResponse(stringBuilder.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<vistaDeNuevoGrupo> newDataList) {
        if (newDataList != null) {
            dataList.addAll(newDataList);
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<vistaDeNuevoGrupo> parseJsonResponse(String json) {
        List<vistaDeNuevoGrupo> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                String nombre = jsonObject.getString("nombrecompleto");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                String urlAnterior = jsonObject.getString("enlacefoto");

                dataList.add(new vistaDeNuevoGrupo(nombre, imageResource, idUsuario, version));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<vistaDeNuevoGrupo> dataList);
    }
}
