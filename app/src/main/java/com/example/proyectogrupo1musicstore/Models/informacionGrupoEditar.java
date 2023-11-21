package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionGrupoEditar {
    private int idgrupo;
    private String nombre;
    private String descripcion;
    private int idowner;
    private int visualizacion;
    private Bitmap image;

    public informacionGrupoEditar(int idgrupo, String nombre, String descripcion, int idowner, int visualizacion, Bitmap image) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idowner = idowner;
        this.visualizacion = visualizacion;
        this.image = image;
    }

    public int getIdgrupo() {return idgrupo;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return descripcion;}

    public int getIdowner() {return idowner;}

    public int getVisualizacion() {return visualizacion;}

    public Bitmap getImage() {return image;}
}
