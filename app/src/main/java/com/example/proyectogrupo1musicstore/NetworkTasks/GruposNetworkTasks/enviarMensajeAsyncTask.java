package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarMensajeAsyncTask extends AsyncTask<String, Void, Void> {

    private final Context context;

    public enviarMensajeAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String jsonData = params[0];
        Integer tipo = Integer.valueOf(params[1]);
        
        

        try {
            URL url = null;
            if (tipo == 1){
                url = new URL("https://phpclusters-156700-0.cloudclusters.net/guardarMensaje.php");
            } else if (tipo == 2) {
                url = new URL("https://phpclusters-156700-0.cloudclusters.net/guardarMensajeUrl.php");
            } else if (tipo == 3) {
                url = new URL("https://phpclusters-156700-0.cloudclusters.net/guardarMensajeVoice.php");
            }

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonData.getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("enviarMensajeAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
