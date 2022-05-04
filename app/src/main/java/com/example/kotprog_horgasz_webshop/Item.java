package com.example.kotprog_horgasz_webshop;

public class Item {

    private String nev;
    private String ar;
    private int image;

    public Item(String nev, String ar, int image) {
        this.nev = nev;
        this.ar = ar;
        this.image = image;
    }

    public Item(String nev, String ar) {
        this.nev = nev;
        this.ar = ar;
    }

    public Item() {
    }

    public String getNev() {
        return nev;
    }

    public String getAr() {
        return ar;
    }

    public int getImage() {
        return image;
    }
}
