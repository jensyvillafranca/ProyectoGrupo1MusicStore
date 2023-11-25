package com.example.proyectogrupo1musicstore.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.proyectogrupo1musicstore.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class GrupoPrivadoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "GrupoPrivadoFirebaseMessagingService";
    private token token = new token(this);
    private int idUsuario;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming messages here
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // Handle the data payload as needed
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // You can use the notification body to display in the app
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
        }
    }

    //Guarda el token en el servidor
    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        String storedToken = retrieveTokenFromSharedPreferences();
        if (storedToken == null) {
            return;
        }
        saveTokenLocally(newToken);
        updateFirebaseToken.updateToken(newToken, idUsuario);
        Log.e("newToken: ", newToken);
    }

    private void saveTokenLocally(String token) {
        // Save the FCM token in SharedPreferences or any other local storage
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }

    private void handleDataMessage(Map<String, String> data) {
        // Handle the data payload (if any)
        // This is where you can process custom data sent from your server
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        String title = notification.getTitle();
        String body = notification.getBody();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.cargando)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            // Handle the case where notifications are disabled
        }
    }

    private String retrieveTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }
}
