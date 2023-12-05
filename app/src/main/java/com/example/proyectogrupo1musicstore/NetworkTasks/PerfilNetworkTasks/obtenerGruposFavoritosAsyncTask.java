package com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Adapters.GruposFavoritosAdapter;
import com.example.proyectogrupo1musicstore.Models.informacionGruposFavoritos;
import com.example.proyectogrupo1musicstore.Models.integrantesItem;
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

public class obtenerGruposFavoritosAsyncTask extends AsyncTask<String, Void, List<informacionGruposFavoritos>> {

    private static final String TAG = "obtenerGruposFavoritosAsyncTask";
    private Context context;
    private GruposFavoritosAdapter adapter;

    private int tipoProgress;

    public obtenerGruposFavoritosAsyncTask(Context context, GruposFavoritosAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected List<informacionGruposFavoritos> doInBackground(String... params) {
        String idusuario = params[0];
        String idusuarioactivo = params[1];
        Integer tipo = Integer.valueOf(params[2]);

        try {
            // construye el URL
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/obtenerGruposFavoritos.php");

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
    protected void onPostExecute(List<informacionGruposFavoritos> dataList) {

        if (dataList != null) {
            adapter.setDataList(dataList);
        }
    }

    private List<informacionGruposFavoritos> parseJsonResponse(String json) {
        List<informacionGruposFavoritos> dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extrae la informacion y crea objetos
                Integer idgrupo = jsonObject.getInt("idgrupo");
                String nombreGrupo = jsonObject.getString("nombre");
                Bitmap imageResource = ImageDownloader.downloadImage(jsonObject.getString("enlacefoto"));
                Integer isMember = jsonObject.getInt("pertenece_al_grupo");

                dataList.add(new informacionGruposFavoritos(idgrupo, nombreGrupo, imageResource, isMember));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }

        return dataList;
    }
}
