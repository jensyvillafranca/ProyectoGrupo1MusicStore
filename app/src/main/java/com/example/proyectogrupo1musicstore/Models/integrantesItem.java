package com.example.proyectogrupo1musicstore.Models;

public class integrantesItem {
    private int imageResId;
    private String itemName;

    public integrantesItem(int imageResId, String itemName) {
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
