package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class buscarUsuario {
    private int idUsuario;
    private String nombre;
    private String usuario;
    private int visualizacion;
    private Bitmap image;
    private String url;
    private int follows;

    public buscarUsuario(int idUsuario, String nombre, String usuario, int visualizacion, Bitmap image, String url, int follows) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.usuario = usuario;
        this.visualizacion = visualizacion;
        this.image = image;
        this.url = url;
        this.follows = follows;
    }

    public int getIdUsuario() {return idUsuario;}

    public String getNombre() {return nombre;}

    public String getUsuario() {return usuario;}

    public int getVisualizacion() {return visualizacion;}

    public Bitmap getImage() {return image;}

    public String getUrl() {return url;}

    public int getFollows() {return follows;}
}
