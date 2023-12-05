package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
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

public class obtenerIntegrantesGrupoAsyncTask extends AsyncTask<String, Void, List<integrantesItem>> {

    private static final String TAG = "obtenerIntegrantesGrupoAsyncTask";
    private Context context;
    private IntegrantesAdapter adapter;
    ProgressDialog progressDialog;

    private int tipoProgress;

    public obtenerIntegrantesGrupoAsyncTask(Context context, IntegrantesAdapter adapter, ProgressDialog progressDialog) {
        this.context = context;
        this.adapter = adapter;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<integrantesItem> doInBackground(String... params) {
        String idGrupo = params[0]; // idgrupo parametro
        String tipo = params[1];

        tipoProgress = Integer.valueOf(tipo);

        try {
            // construye el URL
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/obtenerIntegrantesGrupo.php");

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idgrupo", Integer.valueOf(idGrupo));

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
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<integrantesItem> dataList) {
        if (tipoProgress == 1) {
            progressDialog.dismiss();
        }
        if (dataList != null) {
            adapter.setDataList(dataList);
        }
    }

    private List<integrantesItem> parseJsonResponse(String json) {
        List<integrantesItem> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idusuario = jsonObject.getInt("idusuario");
                String nombreGrupo = jsonObject.getString("nombrecompleto");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));

                dataList.add(new integrantesItem(imageResource, nombreGrupo, idusuario));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }
}
