package com.example.proyectogrupo1musicstore;

import android.graphics.Bitmap;

public class videoItem {
    private Bitmap itemPortadaVideo;
    private String itemNombreVideo;
    private int idVideo;

    public videoItem(Bitmap imageResId, String itemName, int id) {
        this.itemPortadaVideo = imageResId;
        this.itemNombreVideo = itemName;
        this.idVideo = id;
    }

    public Bitmap getImageResId() {
        return itemPortadaVideo;
    }

    public String getItemName() {
        return itemNombreVideo;
    }

    public int getIdVideo() {

        return idVideo;
    }
}
