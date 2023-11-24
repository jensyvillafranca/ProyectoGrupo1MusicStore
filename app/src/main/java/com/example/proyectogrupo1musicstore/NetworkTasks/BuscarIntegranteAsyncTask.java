package com.example.proyectogrupo1musicstore.NetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterNuevoGrupoIntegrantes;
import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;
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

public class BuscarIntegranteAsyncTask extends AsyncTask<String, Void, List<vistaDeNuevoGrupo>> {
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
    protected List<vistaDeNuevoGrupo> doInBackground(String... params) {
        String idusuario = params[0];
        String search = params[1];

        try {
            URL url = new URL("https://phpclusters-152621-0.cloudclusters.net/buscarIntegrantePorNombre.php");
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

                return parseJsonResponse(response.toString());
            } else {
                Log.e("BuscarIntegranteAsyncTask", "Error response code: " + responseCode);
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<vistaDeNuevoGrupo> result) {
        progressDialog.dismiss();

        if (result != null) {
            adapter.setDataList(result);
            adapter.notifyDataSetChanged();
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
