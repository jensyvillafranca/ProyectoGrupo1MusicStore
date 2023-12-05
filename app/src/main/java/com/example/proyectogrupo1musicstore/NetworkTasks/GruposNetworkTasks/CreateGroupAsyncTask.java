package com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectogrupo1musicstore.Activities.Grupos.ActivityGrupoPrincipal;
import com.example.proyectogrupo1musicstore.Utilidades.UI.DelayedActivityStarter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CreateGroupAsyncTask extends AsyncTask<String, Void, Void> {
    private ProgressDialog progressDialog;
    private final Context context;
    private final List<Integer> selectedUserIds;
    private final String groupName;
    private final String groupDescription;
    private final int estado;
    private final EditText textNombreGrupo;
    private final byte[] groupImage;
    private boolean onProgressCalled = false;
    private final int idUsuario;


    public CreateGroupAsyncTask(Context context, List<Integer> selectedUserIds, String groupName, String groupDescription, byte[] groupImage, int estado, EditText textNombreGrupo, int idUsuario) {
        this.context = context;
        this.selectedUserIds = selectedUserIds;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupImage = groupImage;
        this.estado = estado;
        this.textNombreGrupo = textNombreGrupo;
        this.idUsuario = idUsuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Creating group...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        boolean nombreGrupoDisponible = checkGroupExistence(groupName);

        if (nombreGrupoDisponible == false) {
            publishProgress();
            return null;
        } else if (nombreGrupoDisponible == true) {
            try {
                onProgressCalled = false;
                URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/nuevoGrupo.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                // Prepare JSON data
                String jsonData = prepareJsonData();
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(jsonData.getBytes());
                out.flush();
                out.close();

                int responseCode = urlConnection.getResponseCode();
                Log.d("CreateGroupAsyncTask", "Response Code: " + responseCode);

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            if(onProgressCalled==false){
                // Me lleva a otra actividad
                Toast.makeText(context, "¡El Grupo ha sido Creado! Redirigiendo...", Toast.LENGTH_SHORT).show();
                DelayedActivityStarter.startDelayedActivity(context, ActivityGrupoPrincipal.class, 1500);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        textNombreGrupo.setError("Nombre del Grupo ya existe!");
        onProgressCalled = true;
    }

    private String prepareJsonData() {
        try {
            JSONArray jsonUserIds = new JSONArray(selectedUserIds);


            String base64Image = Base64.encodeToString(groupImage, Base64.DEFAULT);

            JSONObject jsonData = new JSONObject();
            jsonData.put("nombre", groupName);
            jsonData.put("descripcion", groupDescription);
            jsonData.put("idusuario", idUsuario); // Replace with the actual user ID
            jsonData.put("idvisualizacion", estado); // Replace with the actual visualizacion ID
            jsonData.put("estadofavorito", 0);
            jsonData.put("imagen", base64Image);
            jsonData.put("integrantes", jsonUserIds);

            return jsonData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkGroupExistence(String groupName) {
        try {
            // Prepara el JSON
            JSONObject jsonData = new JSONObject();
            jsonData.put("nombre", groupName);

            URL url = new URL("https://phpclusters-156700-0.cloudclusters.net/existeGrupo.php");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (InputStream is = connection.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getBoolean("success");
            }
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

