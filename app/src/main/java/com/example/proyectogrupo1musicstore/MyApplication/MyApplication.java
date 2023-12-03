package com.example.proyectogrupo1musicstore.MyApplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.room.Room;

import com.example.proyectogrupo1musicstore.Room.AppDatabase;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    private static AppDatabase appDatabase;
    private static boolean isAppInForeground = false;
    private static boolean isAppRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        //Inicia firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // Initialize Room database
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "notification-db").build();
        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static boolean isAppInForeground() {
        return isAppInForeground;
    }

    public static boolean isAppRunning() {
        return isAppRunning;
    }

    private static class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            // App is created
            isAppRunning = true;
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // App is started
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // App is resumed (in the foreground)
            isAppInForeground = true;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // App is paused (in the background)
            isAppInForeground = false;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // App is stopped
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // Save instance state
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            // App is destroyed
            isAppRunning = false;
        }
    }
}

