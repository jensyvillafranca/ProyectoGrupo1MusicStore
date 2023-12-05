package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.UI.DelayedActivityStarter;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateGrupoAsyncTask extends AsyncTask<String, Void, Void> {

    private final Context context;
    private final ProgressDialog progressDialog;

    public UpdateGrupoAsyncTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Actualizando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        String jsonData = params[0];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/updateInfoGrupo.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonData.getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("UpdateTokenAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "¡Cambios Guardados!", Toast.LENGTH_SHORT).show();
        DelayedActivityStarter.startDelayedActivity(context, ActivityGrupoPrincipal.class, 1500);
        progressDialog.dismiss();
    }
}
