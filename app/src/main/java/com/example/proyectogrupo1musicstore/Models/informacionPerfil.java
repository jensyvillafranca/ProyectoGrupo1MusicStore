package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionPerfil {
    private int idusuario;
    private String nombre;
    private String correo;
    private String usuario;
    private Bitmap foto;
    private int visualizacion;
    private int numeroSeguidores;
    private int numeroSeguidos;
    private int seguidor;

    public informacionPerfil(int idusuario, String nombre, String correo, String usuario, Bitmap foto, int visualizacion, int numeroSeguidores, int numeroSeguidos, int seguidor) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.correo = correo;
        this.usuario = usuario;
        this.foto = foto;
        this.visualizacion = visualizacion;
        this.numeroSeguidores = numeroSeguidores;
        this.numeroSeguidos = numeroSeguidos;
        this.seguidor = seguidor;
    }

    public int getIdusuario() {return idusuario;}

    public String getNombre() {return nombre;}

    public String getCorreo() {return correo;}

    public String getUsuario() {return usuario;}

    public Bitmap getFoto() {return foto;}

    public int getVisualizacion() {return visualizacion;}

    public int getNumeroSeguidores() {return numeroSeguidores;}

    public int getNumeroSeguidos() {return numeroSeguidos;}

    public int getSeguidor() {return seguidor;}
}
