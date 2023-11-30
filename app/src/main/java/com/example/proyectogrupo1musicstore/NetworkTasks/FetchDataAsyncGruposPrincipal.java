package com.example.proyectogrupo1musicstore.NetworkTasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.proyectogrupo1musicstore.Models.vistaDeGrupo;
import com.example.proyectogrupo1musicstore.Utilidades.ImageDownloader;

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

public class FetchDataAsyncGruposPrincipal extends AsyncTask<String, Void, List<vistaDeGrupo>> {

    private static final String TAG = "FetchDataAsyncGruposPrincipal";
    private DataFetchListener dataFetchListener;
    ProgressDialog progressDialog;

    public FetchDataAsyncGruposPrincipal(DataFetchListener listener, ProgressDialog progressDialog) {
        this.dataFetchListener = listener;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<vistaDeGrupo> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idUsuario = params[1]; // idusuario parametro

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
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            inputStream.close();
            // Parse the JSON response
            return parseJsonResponse(stringBuilder.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
            progressDialog.dismiss();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<vistaDeGrupo> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<vistaDeGrupo> parseJsonResponse(String json) {
        List<vistaDeGrupo> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idgrupo = jsonObject.getInt("idgrupo");
                String nombreGrupo = jsonObject.getString("nombre");
                String creador = jsonObject.getString("usuario");
                Integer idOwner = jsonObject.getInt("idusuario");
                Integer miembros = jsonObject.getInt("numeromiembros");
                String url = jsonObject.getString("enlacefoto");
                Bitmap imageResource = ImageDownloader.downloadImage(url);
                Integer estadoFavorito = jsonObject.getInt("estadofavorito");

                dataList.add(new vistaDeGrupo(nombreGrupo, creador, "Integrantes: "+miembros, imageResource, url, idgrupo, estadoFavorito, idOwner));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<vistaDeGrupo> dataList);
    }
}
