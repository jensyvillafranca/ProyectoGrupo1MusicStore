package com.example.proyectogrupo1musicstore.Utilidades.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class DelayedActivityStarter {

    public static void startDelayedActivity(final Context context, final Class<?> targetActivity, int delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the new activity
                Intent intent = new Intent(context, targetActivity);
                context.startActivity(intent);

                // Finish the current activity if needed
                // ((Activity) context).finish(); // Uncomment this line if used in an Activity
            }
        }, delayMillis);
    }
}