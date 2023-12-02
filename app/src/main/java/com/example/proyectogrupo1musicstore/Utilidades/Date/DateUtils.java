package com.example.proyectogrupo1musicstore.Utilidades.Date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // Convert timestamp to human-readable format
    public static String formatTimestamp(long timestamp) {
        // Create a DateFormatter object for displaying date and time in a human-readable format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        // Create a Date object using the timestamp (multiply by 1000 to convert seconds to milliseconds)
        Date date = new Date(timestamp * 1000);

        // Format the date and time
        return formatter.format(date);
    }
}
