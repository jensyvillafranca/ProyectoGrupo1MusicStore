package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoGeneral;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.InfomacionGeneralGrupoAsyncTask;
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

public class inforMusicaPlaylistAsyncTask extends AsyncTask<String, Void, List<infoPlayListMusica>> {
    private static final String TAG = "inforMusicaPlaylistAsyncTask";
    //private DataFetchListener dataFetchListener;
    private DataFetchListener dataFetchListener;

    public inforMusicaPlaylistAsyncTask(DataFetchListener listener) {
        this.dataFetchListener = listener;
    }

    @Override
    protected List<infoPlayListMusica> doInBackground(String... params) {
        String urlString = params[0]; // URL para el microservicio
        String idplaylist = params[1]; // idgrupo parametro

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
            jsonParams.put("idplaylist", Integer.valueOf(idplaylist));

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
    protected void onPostExecute(List<infoPlayListMusica> dataList) {
        if (dataList != null) {
            dataFetchListener.onDataFetched(dataList);
        }
    }

    private List<infoPlayListMusica> parseJsonResponse(String json) {
        List<infoPlayListMusica> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idplaylist = jsonObject.getInt("idplaylist");
                String nombre = jsonObject.getString("nombre");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                //Integer numeroMusica = jsonObject.getInt("numeromusica");


                dataList.add(new infoPlayListMusica(idplaylist, nombre,imageResource ));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
            Log.e(TAG, "JSON response: " + json);
        }

        return dataList;
    }

    // Interface to notify when data is fetched
    public interface DataFetchListener {
        void onDataFetched(List<infoPlayListMusica> dataList);
    }
}
