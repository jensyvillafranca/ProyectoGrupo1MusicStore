package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectogrupo1musicstore.Models.vistaDeNuevoGrupo;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class deleteIntegranteAsyncTask extends AsyncTask<String, Void, Void> {

    private final Context context;
    private final ProgressDialog progressDialog;
    private final RecyclerView recyclerView;
    private final int index;
    private final List<vistaDeNuevoGrupo> dataList;

    public deleteIntegranteAsyncTask(Context context, RecyclerView recyclerView, int index, List<vistaDeNuevoGrupo> list) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.index = index;
        this.dataList = list;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Eliminando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        String jsonData = params[0];

        try {
            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/deleteIntegrante.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(jsonData.getBytes());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("deleteIntegranteAsyncTask", "Response Code: " + responseCode);

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "¡Integrante Eliminado!", Toast.LENGTH_SHORT).show();
        dataList.remove(index);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }
}
