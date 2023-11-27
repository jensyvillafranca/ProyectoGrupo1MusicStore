package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class videoItem {
    private Bitmap imageResId;
    private String itemName;
    private int id;
    private String url;

    public videoItem(Bitmap imageResId, String itemName, int id, String url) {
        this.imageResId = imageResId;
        this.itemName = itemName;
        this.id = id;
        this.url = url;
    }

    public Bitmap getImageResId() {
        return imageResId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getId() {return id;}

    public String getUrl() {return url;}
}
