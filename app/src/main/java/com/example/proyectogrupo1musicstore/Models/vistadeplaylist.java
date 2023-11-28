package com.example.proyectogrupo1musicstore.Models;

import android.graphics.Bitmap;

public class vistadeplaylist {
    private String text1;
    private String text2;
    private String text3;
    private int idplaylist;
    private Bitmap imageResource;
    private int idOwner;



    public vistadeplaylist(String text1, String text2,String text3, int idplaylist, Bitmap imageResource, int idOwner) {
        this.text1 = text1;
        this.text2 = text2;
        this.text2 = text3;
        this.idplaylist = idplaylist;
        this.imageResource = imageResource;
        this.idOwner = idOwner;
    }


    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public int getIdplaylist() {
        return idplaylist;
    }

    public void setIdplaylist(int idplaylist) {
        this.idplaylist = idplaylist;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }

    public void setImageResource(Bitmap imageResource) {
        this.imageResource = imageResource;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
}
