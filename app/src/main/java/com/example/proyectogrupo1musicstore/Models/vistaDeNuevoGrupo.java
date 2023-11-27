package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class vistaDeNuevoGrupo {
    private String text1;
    private int idSeguidor;
    private Bitmap imageResource;

    //para controlar la vista del checkbox o boton eliminar
    private int version;

    public vistaDeNuevoGrupo(String text1, Bitmap imageResource, int idSeguidor, int version) {
        this.text1 = text1;
        this.imageResource = imageResource;
        this.idSeguidor = idSeguidor;
        this.version = version;
    }

    public String getText1() {
        return text1;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }

    public int getIdSeguidor() {return idSeguidor;}

    public void setIdSeguidor(int idSeguidor) {this.idSeguidor = idSeguidor;}

    public int getVersion() {return version;}
}
