package com.example.proyectogrupo1musicstore.Utilidades.Firebase;

import com.example.proyectogrupo1musicstore.NetworkTasks.FirebaseAsyncTask.UpdateTokenAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class updateFirebaseToken {
    public static void updateToken(String token, int idusuario) {
        // Construye el JSON
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("token", token);
            jsonData.put("idusuario", idusuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new UpdateTokenAsyncTask().execute(jsonData.toString());
    }
}
