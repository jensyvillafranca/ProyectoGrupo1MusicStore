package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class buscarGrupo {
    private int idgrupo;
    private String nombre;
    private String descripcion;
    private int idOwner;
    private String usuario;
    private int idvisualizacion;
    private Bitmap image;
    private int totalusuarios;
    private int ismember;

    public buscarGrupo(int idgrupo, String nombre, String descripcion, int idOwner, String usuario, int idvisualizacion, Bitmap image, int totalusuarios, int ismember) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idOwner = idOwner;
        this.usuario = usuario;
        this.idvisualizacion = idvisualizacion;
        this.image = image;
        this.totalusuarios = totalusuarios;
        this.ismember = ismember;
    }

    public int getIdgrupo() {return idgrupo;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return descripcion;}

    public int getIdOwner() {return idOwner;}

    public String getUsuario() {return usuario;}

    public int getIdvisualizacion() {return idvisualizacion;}

    public Bitmap getImage() {return image;}

    public int getTotalusuarios() {return totalusuarios;}

    public int getIsmember() {return ismember;}
}
