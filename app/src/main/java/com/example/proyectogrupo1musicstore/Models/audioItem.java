package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class audioItem {
    private Bitmap itemPortada;
    private String itemNombre;
    private int id;
    private String url;
    private int estadofavorito;
    private int tipoVista;

    public audioItem(Bitmap imageResId, String itemName, int id,String url, int estadofavorito, int tipoVista) {
        this.itemPortada = imageResId;
        this.itemNombre = itemName;
        this.id = id;
        this.url = url;
        this.estadofavorito = estadofavorito;
        this.tipoVista = tipoVista;
    }

    public Bitmap getImageResId() {return itemPortada;}

    public String getItemName() {return itemNombre;}

    public int getId() {
        return id;
    }

    public String getUrl() {return url;}

    public int getEstadofavorito() {return estadofavorito;}

    public int getTipoVista() {return tipoVista;}
}
