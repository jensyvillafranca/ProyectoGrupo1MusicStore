package com.example.proyectogrupo1musicstore.Models;

public class vistaDeNuevoGrupo {
    private String text1;

    private int imageResource;

    public vistaDeNuevoGrupo(String text1, int imageResource) {
        this.text1 = text1;
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }


    public int getImageResource() {
        return imageResource;
    }
}
