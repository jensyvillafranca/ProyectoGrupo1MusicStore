package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateFavoritoAsyncTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String jsonData = params[0];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/updateGrupoFavorito.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonData.getBytes());
            Log.d("prueba JSON", String.valueOf(jsonData));
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("UpdateFavoritoAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

