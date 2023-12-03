package com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.example.proyectogrupo1musicstore.Models.informacionPerfil;
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

public class InformacionPerfilAsyncTask extends AsyncTask<String, Void, List<informacionPerfil>> {

    private static final String TAG = "InformacionPerfilAsyncTask";
    private DataFetchListener dataFetchListener;

    public InformacionPerfilAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }

    @Override
    protected List<informacionPerfil> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idusuario = params[1]; // idgrupo parametro
        String idusuarioactivo = params[2];


        try {
            // construye el URL
            URL url = new URL(urlString);
            Log.d(TAG, "URL: " + url.toString());

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idusuario", Integer.valueOf(idusuario));
            jsonParams.put("idusuarioactivo", Integer.valueOf(idusuarioactivo));

            // Escribe el JSON al output stream
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonParams.toString().getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            Log.d(TAG, "Response Code: " + responseCode + ", Message: " + responseMessage);

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
                Log.e("InformacionPerfilAsyncTask", "Error response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<informacionPerfil> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<informacionPerfil> parseJsonResponse(String json) {
        List<informacionPerfil> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idusuario = jsonObject.getInt("idusuario");
                String nombre = jsonObject.getString("nombrecompleto");
                String correo = jsonObject.getString("correo");
                String usuario = jsonObject.getString("usuario");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                Integer idvisualizacion = jsonObject.getInt("idvisualizacion");
                Integer numeroSeguidores = jsonObject.getInt("numeroseguidores");
                Integer numeroSeguidos = jsonObject.getInt("numeroseguidos");
                Integer seguidor = jsonObject.getInt("sigue_al_asuario");

                dataList.add(new informacionPerfil(idusuario, nombre, correo, usuario, imageResource, idvisualizacion, numeroSeguidores, numeroSeguidos, seguidor));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<informacionPerfil> dataList);
    }
}
