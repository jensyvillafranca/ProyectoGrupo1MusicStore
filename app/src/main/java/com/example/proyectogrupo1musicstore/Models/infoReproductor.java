package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

import java.net.URL;

public class infoReproductor {
    private final int idaudio;
    private  String nombre;
    private  int idowner;
    private  String url;
    private  Bitmap image;

    public infoReproductor(int idaudio, String nombre, int idowner, String url, Bitmap image) {
        this.idaudio = idaudio;
        this.nombre = nombre;
        this.idowner = idowner;
        this.url = url;
        this.image = image;
    }

    public int getIdaudio() {
        return idaudio;
    }

    public String getnombre() {
        return nombre;
    }

    public int getIdowner() {
        return idowner;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }
}
