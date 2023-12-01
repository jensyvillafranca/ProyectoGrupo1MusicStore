package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.os.AsyncTask;

import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloader;

import android.graphics.Bitmap;
import android.util.Log;

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

public class informacionGeneralPlayListAstAsyncTask extends AsyncTask<String, Void, List<PlayListItem>> {

    private static final String TAG = "informacionGeneralPlayListAstAsyncTask";
    private DataFetchListener dataFetchListener;

    public informacionGeneralPlayListAstAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }

    @Override
    protected List<PlayListItem> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
       String idusuario = params[1]; // idplaylist parametro
        //String idplaylist = params[1];
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
                Log.e("informacionGeneralPlayListAstAsyncTask", "Error response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<PlayListItem> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
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
                String nombre = jsonObject.getString("nombre");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                //Integer numeroMusica = jsonObject.getInt("numeromusica");


                dataList.add(new PlayListItem(imageResource, nombre, idplaylist));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
            Log.e(TAG, "JSON response: " + json);
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<PlayListItem> dataList);
    }
}
