package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class vistaDeGrupo {
    private String text1;
    private String text2;
    private String text3;
    private Bitmap imageResource;

    public vistaDeGrupo(String text1, String text2, String text3, Bitmap imageResource) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }
}
