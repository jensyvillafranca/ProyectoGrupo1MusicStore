package com.example.proyectogrupo1musicstore.Utilidades.Imagenes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloaderAsync extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> imageViewReference;

    public ImageDownloaderAsync(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
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

    @Override
    protected void onPostExecute(Bitmap result) {
        ImageView imageView = imageViewReference.get();
        if (imageView != null && result != null) {
            imageView.setImageBitmap(result);
        }
    }
}
