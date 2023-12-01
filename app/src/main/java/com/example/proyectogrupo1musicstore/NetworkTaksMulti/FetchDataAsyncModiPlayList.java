package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Models.vistadeplaylist;
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

public class FetchDataAsyncModiPlayList extends AsyncTask<String, Void, List<vistadeplaylist>> {

    private static final String TAG = "FetchDataAsyncModiPlayList";
    private DataFetchListener dataFetchListener;

    ProgressDialog progressDialog;


    public FetchDataAsyncModiPlayList(DataFetchListener listener, ProgressDialog progressDialog) {
        this.dataFetchListener = listener;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<vistadeplaylist> doInBackground(String... params) {
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
    protected void onPostExecute(List<vistadeplaylist> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<vistadeplaylist> parseJsonResponse(String json) {
        List<vistadeplaylist> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idplaylist = jsonObject.getInt("idplaylist");
                String nombre = jsonObject.getString("nombre");
                String biografia = jsonObject.getString("biografia");
                String creador = jsonObject.getString("usuario");
                Integer idOwner = jsonObject.getInt("idusuario");

                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));

                dataList.add(new vistadeplaylist(nombre, creador,biografia, idplaylist, imageResource , idOwner));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }



    public interface DataFetchListener {
        void onDataFetched(List<vistadeplaylist> dataList);
    }
}
