package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.graphics.Bitmap;

public class infoPlayListMusica {
    private int idgrupo;
    private String nombre;
    private Bitmap foto;

    public infoPlayListMusica(int idgrupo, String nombre, Bitmap foto) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
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
}
