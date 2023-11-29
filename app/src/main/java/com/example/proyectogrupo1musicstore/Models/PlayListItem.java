package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class PlayListItem {
    private Bitmap imageResId;
    private String itemName;
    private int id;
    //private int numero;





    public PlayListItem(Bitmap imageResId, String itemName, int id) {
        this.imageResId = imageResId;
        this.itemName = itemName;
        this.id = id;
       // this.numero = numero;
    }

    public Bitmap getImageResId() {
        return imageResId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getId() {return id;}


}
