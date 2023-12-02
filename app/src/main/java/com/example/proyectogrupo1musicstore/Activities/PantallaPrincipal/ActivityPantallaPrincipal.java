package com.example.proyectogrupo1musicstore.Activities.PantallaPrincipal;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyectogrupo1musicstore.NetworkTasks.aceptarSolicitudAsyncTask;
import com.example.proyectogrupo1musicstore.NetworkTasks.denegarSolicitudAsyncTask;
import com.example.proyectogrupo1musicstore.R;
import com.example.proyectogrupo1musicstore.Room.NotificationEntity;
import com.example.proyectogrupo1musicstore.Utilidades.Navegacion.NavigationClickListener;
import com.example.proyectogrupo1musicstore.Utilidades.Token.token;
import com.example.proyectogrupo1musicstore.Utilidades.Firebase.updateFirebaseToken;
import com.example.proyectogrupo1musicstore.activity_login;
import com.google.firebase.messaging.FirebaseMessaging;
import com.example.proyectogrupo1musicstore.Utilidades.Token.JwtDecoder;

import com.example.proyectogrupo1musicstore.Room.AppDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityPantallaPrincipal extends AppCompatActivity {

    //Otros elementos
    private Button btnNotifications;
    private DrawerLayout drawerLayout;
    private ImageButton openMenuButton;
    //Crea nueva instancia de clase token, para obtener el valor de idusuario de la clase decodetoken
    private token acceso = new token(this);
    private int idUsuario;

    private AppDatabase appDatabase;

    private List<NotificationEntity> notificationList;

    // Declara el launcher de permiso
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        askNotificationPermission();

        notificationList = new ArrayList<>();

        idUsuario = Integer.parseInt(JwtDecoder.decodeJwt(acceso.recuperarTokenFromKeystore()));

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        updateFirebaseToken.updateToken(token, idUsuario);
                        Log.e("Token: ", token);
                    } else {
                        // Handle the error
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        //Inicializacion de elementos
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        openMenuButton = (ImageButton) findViewById(R.id.btn_PrincipalDesplegable);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnNotifications.setOnClickListener(v -> showNotificationsPopup());


        // AsyncTask para obtener las notifications de la base de datos en un hilo de fondo
        new LoadNotificationsAsyncTask(this).execute();

        //Listener para menu de navegacion
        View.OnClickListener buttonClickNav = new NavigationClickListener(this,this);

        //Listener para abrir menu de navegacion
        openMenuButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(findViewById(R.id.side_menu));
        });
    }

    // AsyncTask para cargar las notificaciones de la base de datos en el fondo
    private static class LoadNotificationsAsyncTask extends AsyncTask<Void, Void, List<NotificationEntity>> {
        private WeakReference<ActivityPantallaPrincipal> activityReference;

        LoadNotificationsAsyncTask(ActivityPantallaPrincipal context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<NotificationEntity> doInBackground(Void... voids) {
            // Obtiene todas las notificaciones
            ActivityPantallaPrincipal activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            return activity.appDatabase.notificationDao().getAllNotifications();
        }

        @Override
        protected void onPostExecute(List<NotificationEntity> result) {
            ActivityPantallaPrincipal activity = activityReference.get();
            if (activity == null || activity.isFinishing() || result == null) {
                return;
            }

            activity.notificationList = result;
            activity.btnNotifications.setText("NOTIFICACIONES ( " + activity.notificationList.size() + " )");
        }
    }

    //Metodo para cambiar de actividad
    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    //Metodo para cerrar sesion
    private void cerrarSesion() {
        acceso.borrarToken();
        // Regresar al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
        finish();
    }

    //Creat un canal de notificaciones
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MusisStoreHN";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //Metodo para pedir permiso de notificaciones
    private void askNotificationPermission() {
        // Solo es necesario para API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Preguntar por el permiso directamente
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    //Metodo para mostrar las notificaciones
    private void showNotificationsPopup() {
        PopupMenu popupMenu = new PopupMenu(this, btnNotifications);
        Menu menu = popupMenu.getMenu();

        // Agrega las notificaciones al menu
        for (int i = 0; i < notificationList.size(); i++) {
            menu.add(0, i, i, notificationList.get(i).title);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            // Controla lo que pasa cuando se le da click a una notificacion
            int notificationIndex = item.getItemId();
            handleNotificationButtonClick(notificationIndex);
            return true;
        });

        popupMenu.show();
    }

    private void handleNotificationButtonClick(int notificationIndex) {
        // Obtiene la informacion de la notificacion seleccionada
        NotificationEntity selectedNotification = notificationList.get(notificationIndex);

        // Muestra los detalles de la notificacion dependiendo el tipo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (selectedNotification.tipo.equals("request")) {
            builder.setTitle(selectedNotification.title)
                    .setMessage(selectedNotification.body)
                    .setPositiveButton("Aceptar", (dialog, which) -> {

                        //crea el json
                        JSONObject jsonData = crearJson(selectedNotification.groupid, selectedNotification.userid);

                        new aceptarSolicitudAsyncTask(ActivityPantallaPrincipal.this).execute(jsonData.toString());
                        dialog.dismiss();
                        deleteNotificationFromDatabase(selectedNotification.id);
                    })
                    .setNegativeButton("Denegar", (dialog, which) -> {
                        // Desaparece el dialogo en accion negativa
                        JSONObject jsonData = crearJson(selectedNotification.groupid, selectedNotification.userid);
                        new denegarSolicitudAsyncTask(ActivityPantallaPrincipal.this).execute(jsonData.toString());
                        dialog.dismiss();
                        deleteNotificationFromDatabase(selectedNotification.id);
                    })
                    .show();
        } else {
            builder.setTitle(selectedNotification.title)
                    .setMessage(selectedNotification.body)
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        dialog.dismiss();
                        deleteNotificationFromDatabase(selectedNotification.id);
                    })
                    .show();
        }
    }

    private JSONObject crearJson(Integer groupid, Integer userid){
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("idgrupo", groupid);
            jsonData.put("idusuario", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    //Metodo para eliminar una notificacion mediante async task en el fondo
    private void deleteNotificationFromDatabase(int notificationId) {
        // Use AsyncTask or another background thread to perform database operation
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Perform database operations (delete) in the background
                appDatabase.notificationDao().deleteNotificationById(notificationId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                recreate();
            }
        }.execute();
    }
}