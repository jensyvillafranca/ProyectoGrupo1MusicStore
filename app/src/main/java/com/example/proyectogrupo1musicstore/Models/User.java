package com.example.proyectogrupo1musicstore.Models;

public class User {
    private int idusuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String usuario;
    private String enlacefoto;
    private String seguirseguido;
    private int idVisualizacion;

    // Constructor por defecto
    public User() {
    }
    public User(int idusuario, String nombres, String apellidos, String correo, String usuario, String enlacefoto, String seguirseguido, int idVisualizacion) {
        this.idusuario = idusuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.usuario = usuario;
        this.enlacefoto = enlacefoto;
        this.seguirseguido = seguirseguido;
        this.idVisualizacion = idVisualizacion;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getEnlacefoto() {
        return enlacefoto;
    }

    public void setEnlacefoto(String enlacefoto) {
        this.enlacefoto = enlacefoto;
    }

    public String getSeguirseguido() { return seguirseguido; }

    public void setSeguirseguido(String seguirseguido) { this.seguirseguido = seguirseguido; }

    public int getIdVisualizacion() { return idVisualizacion; }

    public void setIdVisualizacion(int idVisualizacion) { this.idVisualizacion = idVisualizacion; }
}
