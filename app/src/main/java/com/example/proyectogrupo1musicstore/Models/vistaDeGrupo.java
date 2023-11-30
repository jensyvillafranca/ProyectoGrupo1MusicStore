package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class vistaDeGrupo {
    private String text1;
    private String text2;
    private String text3;
    private int idgrupo;
    private int estadofavorito;
    private Bitmap imageResource;
    private String url;
    private int idOwner;

    public vistaDeGrupo(String text1, String text2, String text3, Bitmap imageResource, String url, int idgrupo, int estadofavorito, int idOwner) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.imageResource = imageResource;
        this.url = url;
        this.idgrupo = idgrupo;
        this.estadofavorito = estadofavorito;
        this.idOwner = idOwner;
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

    public int getEstadofavorito() {return estadofavorito;}

    public void setEstadofavorito(int estadofavorito) {this.estadofavorito = estadofavorito;}

    public int getIdgrupo() {return idgrupo;}

    public void setIdgrupo(int idgrupo) {this.idgrupo = idgrupo;}

    public int getIdOwner() {return idOwner;}

    public String getUrl() {return url;}
}
