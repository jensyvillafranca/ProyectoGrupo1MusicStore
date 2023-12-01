package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Models.infoEditarPlayList;
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

public class infoPlayListEditarAsyncTask extends AsyncTask<String, Void, List<infoEditarPlayList>> {
    private static final String TAG = "infoPlayListEditarAsyncTask";
    private DataFetchListener dataFetchListener;
    public infoPlayListEditarAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }
//modificado modificacion
    @Override
    protected List<infoEditarPlayList> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idPlaylist = params[1]; // idplaylist parametro

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
            jsonParams.put("idplaylist", Integer.valueOf(idPlaylist));

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
                Log.e("infoPlayListEditarAsyncTask", "Error response code: " + responseCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo la Información del servidor: " + e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<infoEditarPlayList> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }
    private List<infoEditarPlayList> parseJsonResponse(String json) {
        List<infoEditarPlayList> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idplaylist = jsonObject.getInt("idplaylist");
                String nombre = jsonObject.getString("nombre");
                String biografia = jsonObject.getString("biografia");
                Integer idowner = jsonObject.getInt("idusuario");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                String urlAnterior = jsonObject.getString("enlacefoto");

                dataList.add(new infoEditarPlayList(idplaylist, nombre, biografia, idowner, imageResource, urlAnterior));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }

    public interface DataFetchListener {
        void onDataFetched(List<infoEditarPlayList> dataList);
    }

}


