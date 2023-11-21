package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Adapters.IntegrantesAdapter;
import com.example.proyectogrupo1musicstore.Adapters.PlayListAdapter;
import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
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


public class ObtenerPlayListAsyncTask extends AsyncTask<String, Void, List<PlayListItem>> {

    private static final String TAG = "ObtenerPlayListAsyncTask";
    private Context context;
    private PlayListAdapter adapter;
    ProgressDialog progressDialog;
    private int tipoProgress;

    public ObtenerPlayListAsyncTask(Context context, PlayListAdapter adapter, ProgressDialog progressDialog) {
        this.context = context;
        this.adapter = adapter;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<PlayListItem> doInBackground(String... params) {
        String idplaylist = params[0]; // idplaylist parametro




        try {
            // construye el URL
            URL url = new URL("https://phpclusters-152621-0.cloudclusters.net/obtenerPlayList.php");

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idplaylist", Integer.valueOf(idplaylist));

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
    protected void onPostExecute(List<PlayListItem> dataList) {
        if (dataList != null) {
            adapter.setDataList(dataList);
        }

    }


    private List<PlayListItem> parseJsonResponse(String json) {
        List<PlayListItem> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idplaylist = jsonObject.getInt("idplaylist");
                String nombrePlay = jsonObject.getString("nombreplaylist");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));

                dataList.add(new PlayListItem(imageResource, nombrePlay, idplaylist, 8));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

}
