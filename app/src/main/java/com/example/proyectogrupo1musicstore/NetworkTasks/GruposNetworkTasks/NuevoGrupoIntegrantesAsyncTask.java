package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
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

public class NuevoGrupoIntegrantesAsyncTask extends AsyncTask<String, Void, List<vistaDeNuevoGrupo>> {

    private static final String TAG = "NuevoGrupoIntegrantesAsyncTask";
    private DataFetchListener dataFetchListener;

    private ProgressDialog progressDialog;

    public NuevoGrupoIntegrantesAsyncTask(DataFetchListener listener, ProgressDialog progressDialog) {
        this.dataFetchListener = listener;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<vistaDeNuevoGrupo> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idUsuario = params[1]; // idusuario parametro

        try {
            // construye el URL
            URL url = new URL(urlString);

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
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
    protected void onPostExecute(List<vistaDeNuevoGrupo> dataList) {
        progressDialog.dismiss();
        if (dataList != null) {
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
                Integer idusuario = jsonObject.getInt("idusuario");
                String nombre = jsonObject.getString("nombrecompleto");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));

                dataList.add(new vistaDeNuevoGrupo(nombre, imageResource, idusuario, 1));
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
