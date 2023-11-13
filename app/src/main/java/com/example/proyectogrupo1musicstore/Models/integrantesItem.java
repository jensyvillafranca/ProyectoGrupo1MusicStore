package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class integrantesItem {
    private Bitmap imageResId;
    private String itemName;

    public integrantesItem(Bitmap imageResId, String itemName) {
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
