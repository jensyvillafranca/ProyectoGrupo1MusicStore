package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionGruposFavoritos {
    private Integer idgrupo;
    private String nombre;
    private Bitmap foto;
    private Integer isMember;

    public informacionGruposFavoritos(Integer idgrupo, String nombre, Bitmap foto, Integer isMember) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.foto = foto;
        this.isMember = isMember;
    }

    public Integer getIdgrupo() {return idgrupo;}

    public String getNombre() {return nombre;}

    public Bitmap getFoto() {return foto;}

    public Integer getIsMember() {return isMember;}
}
