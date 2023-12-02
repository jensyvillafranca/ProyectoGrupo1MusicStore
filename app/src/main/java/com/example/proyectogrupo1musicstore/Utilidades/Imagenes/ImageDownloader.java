package com.example.proyectogrupo1musicstore.Utilidades.Imagenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {

    public static Bitmap downloadImage(String imageUrl) {
        try {
            // Crea un objeto URL el URL de la imagen
            URL url = new URL(imageUrl);

            // Abre una conexion al URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Obtiene un InputStream de la conexion
            InputStream input = connection.getInputStream();

            // Decodifica el InputStream a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            // Cierra el InputStream y se desconecta
            input.close();
            connection.disconnect();

            return bitmap;
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return null;
        }
    }
}
