package com.example.proyectogrupo1musicstore.NetworkTasks.PerfilNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal.ActivityPantallaPrincipal;
import com.example.proyectogrupo1musicstore.Activities.Perfil.Activity_PerfilPersonal;
import com.example.proyectogrupo1musicstore.Utilidades.UI.DelayedActivityStarter;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertarSeguidorAsyncTask extends AsyncTask<String, Void, Boolean> {
    private final Context context;

    private final ProgressDialog progressDialog;

    public InsertarSeguidorAsyncTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Porfavor Espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String idusuario = params[0];
        String idusuarioactivo = params[1];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/agregarSeguidor.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("idusuario", idusuario);
            jsonRequest.put("idusuarioactivo", idusuarioactivo);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonRequest.toString().getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("InsertarSeguidorAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            Log.e("InsertarSeguidorAsyncTask", "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        progressDialog.dismiss();
        Toast.makeText(context, "¡Ha seguido al usuario! Redirigiendo...", Toast.LENGTH_SHORT).show();
        DelayedActivityStarter.startDelayedActivity(context, ActivityPantallaPrincipal.class, 1000);
    }
}
