package com.example.proyectogrupo1musicstore.NetworkTasks.Multimedia;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Adapters.AudioAdapter;
import com.example.proyectogrupo1musicstore.Utilidades.Imagenes.ImageDownloader;
import com.example.proyectogrupo1musicstore.Models.audioItem;

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

public class BuscarAudioAsyncTask extends AsyncTask<String, Void, List<audioItem>>{
    private static final String TAG = "BuscarAudioAsyncTask";
    private Context context;
    private RecyclerView recyclerView;
    private AudioAdapter adapter;
    ProgressDialog progressDialog;
    private int tipoProgress;

    public BuscarAudioAsyncTask(Context context, AudioAdapter adapter, ProgressDialog progressDialog) {
        this.context = context;
        this.adapter = adapter;
        this.progressDialog = progressDialog;
    }
    @Override
    protected List<audioItem> doInBackground(String... params) {
        String idUsuario = params[0]; // idgrupo parametro
        String tipo = params[1];
        tipoProgress = Integer.valueOf(tipo);

        try {
            // construye el URL
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/obtenerMusicaUsuario.php");

            // Crea la conexion y la abre
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Crea el objeto JSON con el parametro
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("idUsuario", Integer.valueOf(idUsuario));

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
    protected void onPostExecute(List<audioItem> dataList) {
        if (tipoProgress == 1) {
            progressDialog.dismiss();
        }
        if (dataList != null) {
            adapter.setDataList(dataList);
        }
    }
    private List<audioItem> parseJsonResponse(String json) {
        List<audioItem> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idaudio = jsonObject.getInt("idaudio");
                String nombreCancion = jsonObject.getString("nombrecancion");
                Bitmap enlacePortada = ImageDownloader.downloadImage(jsonObject.getString("enlaceportada"));
                String url = jsonObject.getString("enlaceaudio");
                Integer estadofavorito = jsonObject.getInt("idfavorito");

                dataList.add(new audioItem(enlacePortada, nombreCancion, idaudio, url, estadofavorito, 1));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error JSON response: " + e.getMessage());
        }

        return dataList;
    }

}

