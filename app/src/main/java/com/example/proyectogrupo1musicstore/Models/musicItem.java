package com.example.proyectogrupo1musicstore.Models;

public class musicItem {
    private int imageResId;
    private String itemName;

    public musicItem(int imageResId, String itemName) {
        this.imageResId = imageResId;
        this.itemName = itemName;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getItemName() {
        return itemName;
    }
}
