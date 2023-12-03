package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class informacionGrupoGeneral {
    private int idgrupo;
    private String nombre;
    private Bitmap foto;
    private int numeroMiembros;
    private int numeroMusica;
    private int numeroVideos;
    private String url;

    public informacionGrupoGeneral(int idgrupo, String nombre, Bitmap foto, int numeroMiembros, int numeroMusica, int numeroVideos, String url) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.foto = foto;
        this.numeroMiembros = numeroMiembros;
        this.numeroMusica = numeroMusica;
        this.numeroVideos = numeroVideos;
        this.url = url;
    }

    public informacionGrupoGeneral() {
    }

    public int getIdgrupo() {return idgrupo;}

    public void setIdgrupo(int idgrupo) {this.idgrupo = idgrupo;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public Bitmap getFoto() {return foto;}

    public void setFoto(Bitmap foto) {this.foto = foto;}

    public int getNumeroMiembros() {return numeroMiembros;}

    public void setNumeroMiembros(int numeroMiembros) {this.numeroMiembros = numeroMiembros;}

    public int getNumeroMusica() {return numeroMusica;}

    public void setNumeroMusica(int numeroMusica) {this.numeroMusica = numeroMusica;}

    public int getNumeroVideos() {return numeroVideos;}

    public void setNumeroVideos(int numeroVideos) {this.numeroVideos = numeroVideos;}

    public String getUrl() {return url;}
}
