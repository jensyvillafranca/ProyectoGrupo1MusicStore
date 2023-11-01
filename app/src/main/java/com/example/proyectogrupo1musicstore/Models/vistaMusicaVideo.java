package com.example.proyectogrupo1musicstore.Models;

public class vistaMusicaVideo {

    private String text1;
    private String text2;

    private int imageResource;

    public vistaMusicaVideo(String text1, String text2, int imageResource) {
        this.text1 = text1;
        this.text2 = text2;
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public int getImageResource() {
        return imageResource;
    }
}

