package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class infoEditarPlayList {
    private final int idplaylist;
    private final String nombre;
    private final String biografia;
    private final int idowner;
    private final Bitmap image;
    private final String urlAnterior;

    public infoEditarPlayList(int idplaylist, String nombre, String biografia, int idowner, Bitmap image, String urlAnterior) {
        this.idplaylist = idplaylist;
        this.nombre = nombre;
        this.biografia = biografia;
        this.idowner = idowner;
        this.image = image;
        this.urlAnterior = urlAnterior;
    }

    public int getIdplaylist() {
        return idplaylist;
    }

    public String getNombre() {
        return nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public int getIdowner() {
        return idowner;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getUrlAnterior() {
        return urlAnterior;
    }
}
