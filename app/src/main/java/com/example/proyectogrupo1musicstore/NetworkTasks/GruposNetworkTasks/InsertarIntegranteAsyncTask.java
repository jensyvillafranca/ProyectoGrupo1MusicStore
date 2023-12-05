package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.UI.DelayedActivityStarter;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertarIntegranteAsyncTask extends AsyncTask<String, Void, Boolean> {
    private final Context context;

    private final ProgressDialog progressDialog;
    private String tipoAccion;

    public InsertarIntegranteAsyncTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Porfavor Espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String idgrupo = params[0];
        String idusuario = params[1];
        tipoAccion = params[2];
        String idOwner = params[3];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/insertarIntegrante.php");
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
            if(tipoAccion.equals("Privado")){
                Log.d("InsertarIntegranteAsyncTask", "Solicitud Enviada!");
                Toast.makeText(context, "¡Se ha enviado la solicitud para unirse al grupo! Redirigiendo...", Toast.LENGTH_SHORT).show();
                DelayedActivityStarter.startDelayedActivity(context, ActivityGrupoPrincipal.class, 1500);
            }else{
                Log.d("InsertarIntegranteAsyncTask", "Insertion successful");
                Toast.makeText(context, "¡Se ha unido al grupo! Redirigiendo...", Toast.LENGTH_SHORT).show();
                DelayedActivityStarter.startDelayedActivity(context, ActivityGrupoPrincipal.class, 1500);
            }
        } else {
            Log.e("InsertarIntegranteAsyncTask", "Insertion failed");
        }
    }
}
