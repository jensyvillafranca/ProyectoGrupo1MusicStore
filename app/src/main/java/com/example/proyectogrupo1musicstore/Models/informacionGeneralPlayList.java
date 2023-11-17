package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionGeneralPlayList {
    private int idplaylist;
    private String nombre;
    private Bitmap foto;
    private int numeroMusica;

    public informacionGeneralPlayList() {
    }

    public informacionGeneralPlayList(int idplaylist, String nombre, Bitmap foto, int numeroMusica) {
        this.idplaylist = idplaylist;
        this.nombre = nombre;
        this.foto = foto;
        this.numeroMusica = numeroMusica;
    }

    public int getIdplaylist() {
        return idplaylist;
    }

    public void setIdplaylist(int idplaylist) {
        this.idplaylist = idplaylist;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public int getNumeroMusica() {
        return numeroMusica;
    }

    public void setNumeroMusica(int numeroMusica) {
        this.numeroMusica = numeroMusica;
    }
}
