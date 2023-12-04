package com.example.proyectogrupo1musicstore;

import android.graphics.Bitmap;

public class audioItem {
    private Bitmap itemPortada;
    private String itemNombre;
    private int id;
    private String url;

    public audioItem(Bitmap imageResId, String itemName, int id,String url) {
        this.itemPortada = imageResId;
        this.itemNombre = itemName;
        this.id = id;
        this.url = url;
    }

    public Bitmap getImageResId() {

        return itemPortada;
    }

    public String getItemName() {

        return itemNombre;
    }

    public int getId() {
        return id;
    }
    public String getUrl() {return url;}
}
