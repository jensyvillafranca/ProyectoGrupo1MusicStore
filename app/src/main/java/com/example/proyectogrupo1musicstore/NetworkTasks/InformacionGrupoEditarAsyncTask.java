package com.example.proyectogrupo1musicstore.NetworkTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Models.informacionGrupoEditar;
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

public class InformacionGrupoEditarAsyncTask extends AsyncTask<String, Void, List<informacionGrupoEditar>> {

    private static final String TAG = "InfomacionGrupoEditarAsyncTask";
    private DataFetchListener dataFetchListener;

    public InformacionGrupoEditarAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }

    @Override
    protected List<informacionGrupoEditar> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idgrupo = params[1]; // idgrupo parametro

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
            jsonParams.put("idgrupo", Integer.valueOf(idgrupo));

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
                Log.e("InformacionGeneralGrupoAsyncTask", "Error response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<informacionGrupoEditar> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<informacionGrupoEditar> parseJsonResponse(String json) {
        List<informacionGrupoEditar> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idgrupo = jsonObject.getInt("idgrupo");
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                Integer idowner = jsonObject.getInt("idusuario");
                Integer visualizacion = jsonObject.getInt("idvisualizacion");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));

                dataList.add(new informacionGrupoEditar(idgrupo, nombre, descripcion, idowner, visualizacion, imageResource));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<informacionGrupoEditar> dataList);
    }
}
