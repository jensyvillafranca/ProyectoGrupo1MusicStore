package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class videoItem {
    private Bitmap imageResId;
    private String itemName;

    public videoItem(Bitmap imageResId, String itemName) {
        this.imageResId = imageResId;
        this.itemName = itemName;
    }

    public Bitmap getImageResId() {
        return imageResId;
    }

    public String getItemName() {
        return itemName;
    }
}
