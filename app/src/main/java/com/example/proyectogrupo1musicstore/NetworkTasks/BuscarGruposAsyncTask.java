package com.example.proyectogrupo1musicstore.NetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectogrupo1musicstore.Adapters.CustomAdapterBuscarGrupos;
import com.example.proyectogrupo1musicstore.Models.buscarGrupo;
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

public class BuscarGruposAsyncTask extends AsyncTask<String, Void, List<buscarGrupo>> {
    private static final String TAG = "BuscarGrupoAsyncTask";
    private Context context;
    private RecyclerView recyclerView;
    private CustomAdapterBuscarGrupos adapter;
    private ProgressDialog progressDialog;

    public BuscarGruposAsyncTask(Context context, RecyclerView recyclerView, CustomAdapterBuscarGrupos adapter) {
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
        //progressDialog.show();
    }

    @Override
    protected List<buscarGrupo> doInBackground(String... params) {
        String idusuario = params[0];
        String search = params[1];

        try {
            URL url = new URL("https://phpclusters-152621-0.cloudclusters.net/buscarGrupo.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("nombre", search);
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

                return parseJsonResponse(response.toString());
            } else {
                Log.e(TAG, "Error response code: " + responseCode);
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<buscarGrupo> result) {
        progressDialog.dismiss();

        if (result != null) {
            adapter.setDataList(result);
            adapter.notifyDataSetChanged();
        }
    }

    private List<buscarGrupo> parseJsonResponse(String json) {
        List<buscarGrupo> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idgrupo = jsonObject.getInt("idgrupo");
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                Integer idOwner = jsonObject.getInt("idusuario");
                String usuario = jsonObject.getString("usuario");
                Integer idvisualizacion = jsonObject.getInt("idvisualizacion");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                Integer totalusuarios = jsonObject.getInt("totalusuarios");
                Integer ismember = jsonObject.getInt("ismember");


                dataList.add(new buscarGrupo(idgrupo, nombre, descripcion,idOwner, usuario, idvisualizacion, imageResource, totalusuarios, ismember));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }
}
