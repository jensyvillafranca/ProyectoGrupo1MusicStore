package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
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

public class BuscarIntegranteAsyncTask extends AsyncTask<String, Void, Pair<Integer, List<vistaDeNuevoGrupo>>> {
    private static final String TAG = "BuscarIntegrantesAsyncTask";
    private Context context;
    private RecyclerView recyclerView;
    private CustomAdapterNuevoGrupoIntegrantes adapter;
    private ProgressDialog progressDialog;

    public BuscarIntegranteAsyncTask(Context context, RecyclerView recyclerView, CustomAdapterNuevoGrupoIntegrantes adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Buscando...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Pair<Integer, List<vistaDeNuevoGrupo>> doInBackground(String... params) {
        String idusuario = params[0];
        String search = params[1];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/buscarIntegrantePorNombre.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("idusuario", idusuario);
            jsonRequest.put("nombre", search);

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

                List<vistaDeNuevoGrupo> result = parseJsonResponse(response.toString());
                urlConnection.disconnect();
                return new Pair<>(responseCode, result);
            } else {
                Log.e("BuscarIntegranteAsyncTask", "Error response code: " + responseCode);
                urlConnection.disconnect();
                return new Pair<>(responseCode, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(HttpURLConnection.HTTP_INTERNAL_ERROR, null);
    }

    @Override
    protected void onPostExecute(Pair<Integer, List<vistaDeNuevoGrupo>> result) {
        progressDialog.dismiss();

        int responseCode = result.first;
        List<vistaDeNuevoGrupo> data = result.second;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Parse and process the JSON data
            if (data != null) {
                adapter.setDataList(data);
                adapter.notifyDataSetChanged();
            }
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            Toast.makeText(context, "No se encontró ningún seguidor.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error de Servidor.", Toast.LENGTH_SHORT).show();
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
}
