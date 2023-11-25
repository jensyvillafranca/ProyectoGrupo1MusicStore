package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionGrupoEditar {
    private final int idgrupo;
    private final String nombre;
    private final String descripcion;
    private final int idowner;
    private final int visualizacion;
    private final Bitmap image;
    private final String urlAnterior;

    public informacionGrupoEditar(int idgrupo, String nombre, String descripcion, int idowner, int visualizacion, Bitmap image, String urlAnterior) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idowner = idowner;
        this.visualizacion = visualizacion;
        this.image = image;
        this.urlAnterior = urlAnterior;
    }

    public int getIdgrupo() {return idgrupo;}

    public String getNombre() {return nombre;}

    public String getDescripcion() {return descripcion;}

    public int getIdowner() {return idowner;}

    public int getVisualizacion() {return visualizacion;}

    public Bitmap getImage() {return image;}

    public String getUrlAnterior() {return urlAnterior;}
}
