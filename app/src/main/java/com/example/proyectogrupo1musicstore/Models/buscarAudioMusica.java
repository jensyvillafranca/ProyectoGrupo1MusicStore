package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class buscarAudioMusica {
    private int idaudio;
    private String nombre;
    private String autor;
    private Bitmap image;
    private String genero;

    public buscarAudioMusica(int idaudio, String nombre, String autor, Bitmap image, String genero) {
        this.idaudio = idaudio;
        this.nombre = nombre;
        this.autor = autor;
        this.image = image;
        this.genero = genero;
    }

    public int getIdaudio() {
        return idaudio;
    }

    public void setIdaudio(int idaudio) {
        this.idaudio = idaudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
