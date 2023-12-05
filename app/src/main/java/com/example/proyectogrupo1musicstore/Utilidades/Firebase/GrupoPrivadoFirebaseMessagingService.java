package com.example.proyectogrupo1musicstore.Utilidades.Firebase;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.proyectogrupo1musicstore.MainActivity;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.MyApplication.MyApplication;
import com.example.proyectogrupo1musicstore.Room.NotificationEntity;
import com.example.proyectogrupo1musicstore.Utilidades.AppPreferences.AppPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class GrupoPrivadoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "GrupoPrivadoFirebaseMessagingService";
    private int idUsuario;
    private String tipo;
    private int notificationValue;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Aqui llegan los mensajes
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Aqui llegan los mensajes
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            tipo="request";

            // Controla el payload que contiene la notificacion
            handleDataMessage(remoteMessage.getData(), remoteMessage.getNotification());
        }else{
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                tipo = "simple";

                // Notificacion sin payload
                handleNotification(remoteMessage.getNotification());
            }
        }

        //App is not running
        if (!MyApplication.isAppRunning()==false) {
            handleNotificationClickAction(remoteMessage);
        }

        /* Check if the app is in the foreground or not running
        if (MyApplication.isAppInForeground() == false || !MyApplication.isAppRunning()) {

        } else {
            handleNotificationClickAction(remoteMessage);
        }*/
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

    //Guarda el token localmente
    private void saveTokenLocally(String token) {
        // Save the FCM token in SharedPreferences or any other local storage
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }

    //Metodo que controla el comportamiento de una notificacion con payload
    private void handleDataMessage(Map<String, String> data, RemoteMessage.Notification notification) {
        //Aplica valores
        String title = notification.getTitle();
        String body = notification.getBody();
        Integer idUsuario = Integer.valueOf(data.get("idusuario"));
        Integer idGrupo = Integer.valueOf(data.get("idgrupo"));
        String usuario = data.get("usuario");

        // Guarda la notificacion al local Room database
        saveNotificationToDatabase(title, body, idUsuario, idGrupo, usuario);

        notificationValue = AppPreferences.getUserScore(this);
        if(notificationValue==1){
            //Muestra la notificaion en el tray
            displayNotification(title, body);
        }
    }

    //Metodo que controla el comportamiento de una notificacion sin payload
    private void handleNotification(RemoteMessage.Notification notification) {
        String title = notification.getTitle();
        String body = notification.getBody();

        // Guarda la notificacion al local Room database
        saveNotificationToDatabase(title, body, null, null, null);

        notificationValue = AppPreferences.getUserScore(this);
        if(notificationValue==1){
            // Muestra la notificacion en el tray
            displayNotification(title, body);
        }
    }

    //Metodo para guardar la notificacion en la base de datos Room
    private void saveNotificationToDatabase(String title, String body, Integer idUsuario, Integer idGrupo, String usuario) {
        // Crea un nuevo modelo NotificationEntity
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.title = title;
        notificationEntity.body = body;
        notificationEntity.userid = idUsuario;
        notificationEntity.groupid = idGrupo;
        notificationEntity.username = usuario;
        notificationEntity.tipo = tipo;

        // Agrega la notificacion
        MyApplication.getAppDatabase().notificationDao().insertNotification(notificationEntity);
    }

    private void handleNotificationClickAction(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This ensures that the existing instance of ActivityPantallaPrincipal is brought to the front.

        // You can also add any additional data from the notification to the intent if needed
        // For example, if you have some data in the notification payload:
        // String someData = remoteMessage.getData().get("some_key");
        // intent.putExtra("extra_key", someData);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.cargando)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // Set the PendingIntent
                .setAutoCancel(true); // Auto-cancel the notification when clicked

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notificationBuilder.build());
    }

    //Metodo para mostrar la notificacion en el tray
   private void displayNotification(String title, String body) {
        // Construye y muestra la notificacion
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

    //Metodo para obtener el token de los SharedPreferences
    private String retrieveTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }
}
