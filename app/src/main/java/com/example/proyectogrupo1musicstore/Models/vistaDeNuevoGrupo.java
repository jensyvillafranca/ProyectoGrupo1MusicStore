package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class vistaDeNuevoGrupo {
    private String text1;

    private Bitmap imageResource;

    public vistaDeNuevoGrupo(String text1, Bitmap imageResource) {
        this.text1 = text1;
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }


    public Bitmap getImageResource() {
        return imageResource;
    }
}
