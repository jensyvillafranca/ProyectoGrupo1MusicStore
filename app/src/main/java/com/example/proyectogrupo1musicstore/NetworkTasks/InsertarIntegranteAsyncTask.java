package com.example.proyectogrupo1musicstore.NetworkTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertarIntegranteAsyncTask extends AsyncTask<String, Void, Boolean> {
    private Context context;

    public InsertarIntegranteAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String idgrupo = params[0];
        String idusuario = params[1];
        String tipoAccion = params[2];
        String idOwner = params[3];

        try {
            URL url = new URL("https://phpclusters-152621-0.cloudclusters.net/insertarIntegrante.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("idgrupo", idgrupo);
            jsonRequest.put("idusuario", idusuario);

            if(tipoAccion == "Privado"){
                jsonRequest.put("request_type", "membership_request");
                jsonRequest.put("idOwner", idOwner);
            }

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonRequest.toString().getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("InsertarIntegranteAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("InsertarIntegranteAsyncTask", "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Log.d("InsertarIntegranteAsyncTask", "Insertion successful");
            //startGrupoPrincipalActivity();
        } else {
            Log.e("InsertarIntegranteAsyncTask", "Insertion failed");
        }
    }

    private void startGrupoPrincipalActivity() {
        // Empieza la actividad: ActivityGrupoPrincipal
        Intent intent = new Intent(context, ActivityGrupoPrincipal.class);
        context.startActivity(intent);
    }
}
